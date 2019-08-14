package hu.me.iit.malus.thesis.course.service.impl;

import hu.me.iit.malus.thesis.course.client.FeedbackClient;
import hu.me.iit.malus.thesis.course.client.TaskClient;
import hu.me.iit.malus.thesis.course.client.UserClient;
import hu.me.iit.malus.thesis.course.client.dto.Student;
import hu.me.iit.malus.thesis.course.client.dto.Teacher;
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
 * @author Javorek Dénes
 */
@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final InvitationRepository invitationRepository;

    /**
     * Instantiates a new Course service.
     *
     * @param courseRepository the course repository
     * @param invitationRepository the invitation repository
     */
    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, InvitationRepository invitationRepository) {
        this.courseRepository = courseRepository;
        this.invitationRepository = invitationRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course create(Course course) {
        course.setCreationDate(new Date());
        Teacher teacher = UserClient.getTeacherById(course.getCreator().getId());
        Course newCourse = courseRepository.save(course);
        teacher.getCreatedCourseIds().add(newCourse.getId());
        UserClient.save(teacher);
        log.info("Created course: {}", course);
        return newCourse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course edit(Course course) {
        //TODO what do we want to edit here? basic course fields are easy
        // comment(history)/creator(history)/task(flagged as done) editing has no purpose
        // student editing sounds mind shattering if you think about it
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
        Optional<Course> opt = courseRepository.findById(courseId);
        if (opt.isPresent()) {
            for (Teacher teacher : UserClient.getAllTeachers()) {
                for (Long createdCourseId : teacher.getCreatedCourseIds()) {
                    if (createdCourseId.equals(courseId)) {
                        creator = teacher;
                        break;
                    }
                }
            }
            for (Student student : UserClient.getAllStudents()) {
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
            course.setTasks(TaskClient.getAllByCourseId(courseId));
            course.setComments(FeedbackClient.getByCourseId(courseId));
            return course;
        } else {
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
            for (Teacher teacher : UserClient.getAllTeachers()) {
                for (Long createdCourseId : teacher.getCreatedCourseIds()) {
                    if (createdCourseId.equals(course.getId())) {
                        course.setCreator(teacher);
                    }
                }
            }
            students = new HashSet<>();
            for (Student student : UserClient.getAllStudents()) {
                for (Long assignedCourseId : student.getAssignedCourseIds()) {
                    if (assignedCourseId.equals(course.getId())) {
                        students.add(student);
                    }
                }
            }
            course.setStudents(students);
            course.setTasks(TaskClient.getAllByCourseId(course.getId()));
            course.setComments(FeedbackClient.getByCourseId(course.getId()));
        }
        return courses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invite(Long courseId, String studentId) {
        String invitationUuid = UUID.randomUUID().toString(); // for the email
        invitationRepository.save(new Invitation(invitationUuid, studentId, courseId));
        //TODO send email with email service
        //TODO remove this from the database after 24 hours - FOR DENIS
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invite(Long courseId, List<String> studentIds) {
        List<String> invitationUuids = new ArrayList<>(); //for the emails
        List<Invitation> invitations = new ArrayList<>();
        for (String studentId : studentIds) {
            String uuid = UUID.randomUUID().toString();
            invitationUuids.add(uuid);
            invitations.add(new Invitation(uuid, studentId, courseId));
        }
        invitationRepository.saveAll(invitations);
        //TODO send emails with email service
        //TODO remove these from the database after 24 hours - FOR DENIS
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acceptInvite(String inviteUUID) throws InvitationNotFoundException {
        Optional<Invitation> opt = invitationRepository.findById(inviteUUID);
        if (opt.isPresent()) {
            Invitation invitation = opt.get();
            Student student = UserClient.getStudentById(invitation.getStudentId());
            student.getAssignedCourseIds().add(invitation.getCourseId());
            UserClient.save(student);
            invitationRepository.delete(invitation);
        } else {
            throw new InvitationNotFoundException();
        }
    }
}
