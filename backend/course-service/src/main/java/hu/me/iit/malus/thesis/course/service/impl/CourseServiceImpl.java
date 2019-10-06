package hu.me.iit.malus.thesis.course.service.impl;

import hu.me.iit.malus.thesis.course.client.FeedbackClient;
import hu.me.iit.malus.thesis.course.client.FileManagementClient;
import hu.me.iit.malus.thesis.course.client.TaskClient;
import hu.me.iit.malus.thesis.course.client.UserClient;
import hu.me.iit.malus.thesis.course.client.dto.Student;
import hu.me.iit.malus.thesis.course.client.dto.Teacher;
import hu.me.iit.malus.thesis.course.controller.dto.CourseDto;
import hu.me.iit.malus.thesis.course.model.Course;
import hu.me.iit.malus.thesis.course.model.Invitation;
import hu.me.iit.malus.thesis.course.repository.CourseRepository;
import hu.me.iit.malus.thesis.course.repository.InvitationRepository;
import hu.me.iit.malus.thesis.course.service.CourseService;
import hu.me.iit.malus.thesis.course.service.exception.CourseNotFoundException;
import hu.me.iit.malus.thesis.course.service.exception.InvitationNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Default implementation for Course service.
 *
 * @author Attila Szőke
 */
@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    private CourseRepository courseRepository;
    private InvitationRepository invitationRepository;
    private TaskClient taskClient;
    private FeedbackClient feedbackClient;
    private UserClient userClient;
    private FileManagementClient fileManagementClient;

    /**
     * Instantiates a new Course service.
     * @param courseRepository the course repository
     * @param invitationRepository the invitation repository
     * @param taskClient the task client
     * @param feedbackClient the feedback client
     */
    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, InvitationRepository invitationRepository,
                             TaskClient taskClient, FeedbackClient feedbackClient, UserClient userClient,
                             FileManagementClient fileManagementClient) {
        this.courseRepository = courseRepository;
        this.invitationRepository = invitationRepository;
        this.taskClient = taskClient;
        this.feedbackClient = feedbackClient;
        this.userClient = userClient;
        this.fileManagementClient = fileManagementClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course create(Course course) {
        Teacher teacher = userClient.getTeacherByEmail(course.getCreator().getEmail());
        Course newCourse = courseRepository.save(course);
        teacher.getCreatedCourseIds().add(newCourse.getId());
        userClient.saveTeacher(teacher);
        log.info("Created course: {}", newCourse);
        return newCourse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course edit(Course course) {
        log.info("Modified course: {}", course);
        return courseRepository.save(course);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course get(Long courseId) throws CourseNotFoundException {
        Teacher creator = null;
        Set<Student> students = new HashSet<>();
        Set<Teacher> teachers = userClient.getAllTeachers();
        Optional<Course> opt = courseRepository.findById(courseId);
        if (opt.isPresent()) {
            for (Teacher teacher : teachers) {
                for (Long createdCourseId : teacher.getCreatedCourseIds()) {
                    if (createdCourseId.equals(courseId)) {
                        creator = teacher;
                        break;
                    }
                }
            }
            for (Student student : userClient.getAllStudents()) {
                for (Long assignedCourseId : student.getAssignedCourseIds()) {
                    if (assignedCourseId.equals(courseId)) {
                        students.add(student);
                        break;
                    }
                }
            }
            Course course = opt.get();
            course.setCreator(creator);
            course.setStudents(students);
            course.setTasks(taskClient.getAllTasks(courseId));
            course.setFiles(fileManagementClient.getAllFilesByTagId(hu.me.iit.malus.thesis.course.client.dto.Service.COURSE, courseId).getBody());
            course.setComments(feedbackClient.getAllCourseComments(courseId));
            log.info("Course found: {}", courseId);
            return course;
        } else {
            log.error("No course found with this email: {}", courseId);
            throw new CourseNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<Course> getAll() {
        Set<Student> students;
        Iterable<Course> courses = courseRepository.findAll();
        for (Course course : courses) {
            for (Teacher teacher : userClient.getAllTeachers()) {
                for (Long createdCourseId : teacher.getCreatedCourseIds()) {
                    if (createdCourseId.equals(course.getId())) {
                        course.setCreator(teacher);
                    }
                }
            }
            students = new HashSet<>();
            for (Student student : userClient.getAllStudents()) {
                for (Long assignedCourseId : student.getAssignedCourseIds()) {
                    if (assignedCourseId.equals(course.getId())) {
                        students.add(student);
                    }
                }
            }
            course.setStudents(students);
            course.setTasks(taskClient.getAllTasks(course.getId()));
            course.setComments(feedbackClient.getAllCourseComments(course.getId()));
            course.setFiles(fileManagementClient.getAllFilesByTagId(hu.me.iit.malus.thesis.course.client.dto.Service.COURSE, course.getId()).getBody());
        }
        log.info("Courses found: {}", courses);
        return courses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invite(Long courseId, String studentEmail) {
        String invitationUuid = UUID.randomUUID().toString(); // for the email
        invitationRepository.save(new Invitation(invitationUuid, studentEmail, courseId));
        //TODO send email with email service
        //TODO remove this from the database after 24 hours - FOR DENIS
        log.info("Invitation saved to database and e-mail sent - courseId: {}, studentId{}", courseId, studentEmail);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invite(Long courseId, List<String> studentEmails) {
        List<String> invitationUuids = new ArrayList<>(); //for the emails
        List<Invitation> invitations = new ArrayList<>();
        for (String studentId : studentEmails) {
            String uuid = UUID.randomUUID().toString();
            invitationUuids.add(uuid);
            invitations.add(new Invitation(uuid, studentId, courseId));
        }
        invitationRepository.saveAll(invitations);
        //TODO send emails with email service
        //TODO remove these from the database after 24 hours - FOR DENIS
        log.info("Invitations saved to database and e-mails sent - courseId: {}, studentId{}", courseId, studentEmails);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acceptInvite(String inviteUUID) throws InvitationNotFoundException {
        Optional<Invitation> opt = invitationRepository.findById(inviteUUID);
        if (opt.isPresent()) {
            Invitation invitation = opt.get();
            Student student = userClient.getStudentByEmail(invitation.getStudentId());
            student.getAssignedCourseIds().add(invitation.getCourseId());
            userClient.saveStudent(student);
            log.info("Invitation accepted: {}", invitation);
            invitationRepository.delete(invitation);
        } else {
            log.warn("Invitation not found: {}", inviteUUID);
            throw new InvitationNotFoundException();
        }
    }

}
