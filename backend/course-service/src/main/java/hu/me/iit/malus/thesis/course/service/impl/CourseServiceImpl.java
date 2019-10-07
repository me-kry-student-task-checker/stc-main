package hu.me.iit.malus.thesis.course.service.impl;

import hu.me.iit.malus.thesis.course.client.FeedbackClient;
import hu.me.iit.malus.thesis.course.client.FileManagementClient;
import hu.me.iit.malus.thesis.course.client.TaskClient;
import hu.me.iit.malus.thesis.course.client.UserClient;
import hu.me.iit.malus.thesis.course.client.dto.Student;
import hu.me.iit.malus.thesis.course.client.dto.Teacher;
import hu.me.iit.malus.thesis.course.client.dto.User;
import hu.me.iit.malus.thesis.course.client.dto.UserRole;
import hu.me.iit.malus.thesis.course.model.Course;
import hu.me.iit.malus.thesis.course.model.Invitation;
import hu.me.iit.malus.thesis.course.model.exception.ForbiddenCourseEdit;
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
    public Course create(Course course, String creatorsEmail) {
        Teacher teacher = userClient.getTeacherByEmail(creatorsEmail);
        course.setCreator(teacher);

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
    public Course edit(Course course, String editorsEmail) {
        Course oldCourse = courseRepository.getOne(course.getId());

        if (!oldCourse.getCreator().getEmail().equals(editorsEmail)) {
            throw new ForbiddenCourseEdit();
        }

        log.info("Modified course: {}", course);
        return courseRepository.save(course);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course get(Long courseId, String userEmail) throws CourseNotFoundException {
        Optional<Course> optCourse = courseRepository.findById(courseId);

        if (optCourse.isPresent()) {
            Course course = optCourse.get();
            if (!findRelatedCourseIds(userEmail).contains(course.getId())) {
                throw new CourseNotFoundException();
            }
            course.setCreator(userClient.getTeacherByCreatedCourseId(courseId));
            course.setStudents(userClient.getStudentsByAssignedCourseId(courseId));
            course.setTasks(taskClient.getAllTasks(courseId));
            course.setFiles(fileManagementClient.getAllFilesByTagId(hu.me.iit.malus.thesis.course.client.dto.Service.COURSE, courseId).getBody());
            course.setComments(feedbackClient.getAllCourseComments(courseId));
            log.info("Course found: {}", courseId);
            return course;
        } else {
            log.warn("No course found with this id: {}", courseId);
            throw new CourseNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Course> getAll(String userEmail) {
        List<Course> courses = courseRepository.findAll();
        for (Course course : courses) {
            if (!findRelatedCourseIds(userEmail).contains(course.getId())) {
                continue;
            }
            course.setCreator(userClient.getTeacherByCreatedCourseId(course.getId()));
            course.setStudents(userClient.getStudentsByAssignedCourseId(course.getId()));
            course.setTasks(taskClient.getAllTasks(course.getId()));
            course.setComments(feedbackClient.getAllCourseComments(course.getId()));
            course.setFiles(fileManagementClient.getAllFilesByTagId(hu.me.iit.malus.thesis.course.client.dto.Service.COURSE, course.getId()).getBody());
        }
        log.info("Get all courses done, total number of courses is {}", courses.size());
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

    private List<Long> findRelatedCourseIds(String userEmail) {
        User user = userClient.getUserByEmail(userEmail);

        if (user.getRole().equals(UserRole.TEACHER)) {
            Teacher teacher = userClient.getTeacherByEmail(userEmail);
            return teacher.getCreatedCourseIds();
        } else if (user.getRole().equals(UserRole.STUDENT)) {
            Student student = userClient.getStudentByEmail(userEmail);
            return student.getAssignedCourseIds();
        }
        return Collections.emptyList();
    }
}
