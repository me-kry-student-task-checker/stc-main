package hu.me.iit.malus.thesis.course.service.impl;

import hu.me.iit.malus.thesis.course.client.*;
import hu.me.iit.malus.thesis.course.client.dto.Mail;
import hu.me.iit.malus.thesis.course.client.dto.Student;
import hu.me.iit.malus.thesis.course.model.Course;
import hu.me.iit.malus.thesis.course.model.Invitation;
import hu.me.iit.malus.thesis.course.model.exception.ForbiddenCourseEdit;
import hu.me.iit.malus.thesis.course.repository.CourseRepository;
import hu.me.iit.malus.thesis.course.repository.InvitationRepository;
import hu.me.iit.malus.thesis.course.service.CourseService;
import hu.me.iit.malus.thesis.course.service.exception.CourseNotFoundException;
import hu.me.iit.malus.thesis.course.service.exception.InvitationNotFoundException;
import hu.me.iit.malus.thesis.course.service.impl.config.InvitationConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    private EmailClient emailClient;
    private InvitationConfig invitationConfig;

    /**
     * Instantiates a new Course service.
     * @param courseRepository the course repository
     * @param invitationRepository the invitation repository
     * @param taskClient the task client
     * @param feedbackClient the feedback client
     * @param userClient the user client
     * @param fileManagementClient the fileManagement client
     * @param emailClient the email client
     * @param invitationConfig configuration for sending invitations
     */
    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, InvitationRepository invitationRepository,
                             TaskClient taskClient, FeedbackClient feedbackClient, UserClient userClient,
                             FileManagementClient fileManagementClient, EmailClient emailClient,
                             InvitationConfig invitationConfig) {
        this.courseRepository = courseRepository;
        this.invitationRepository = invitationRepository;
        this.taskClient = taskClient;
        this.feedbackClient = feedbackClient;
        this.userClient = userClient;
        this.fileManagementClient = fileManagementClient;
        this.emailClient = emailClient;
        this.invitationConfig = invitationConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course create(Course course, String creatorsEmail) {
        course.setCreationDate(Date.from(Instant.now()));
        Course newCourse = courseRepository.save(course);
        userClient.saveCourseCreation(newCourse.getId());
        log.info("Created course: {}", newCourse.getId());
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

        log.info("Modified course: {}", course.getId());
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
            if (!userClient.isRelated(course.getId())) {
                throw new CourseNotFoundException();
            }

            //TODO: We should find a way to fire these as async requests
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
    public Set<Course> getAll(String userEmail) {
        Set<Long> relatedCourseIds = userClient.getRelatedCourseIds();
        Set<Course> relatedCourses = courseRepository.findAllByIdIsIn(relatedCourseIds);

        for (Course course : relatedCourses) {
            course.setCreator(userClient.getTeacherByCreatedCourseId(course.getId()));
        }
        log.info("Get all courses done, total number of courses is {}", relatedCourses.size());
        return relatedCourses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invite(Long courseId, String studentEmail) {
        String invitationUuid = UUID.randomUUID().toString();
        sendInvitationEmail(invitationUuid, studentEmail);
        invitationRepository.save(Invitation.of(invitationUuid, studentEmail, courseId));

        log.info("Invitation saved to database and e-mail sent - courseId: {}, studentEmail{}", courseId, studentEmail);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invite(Long courseId, List<String> studentEmails) {
        List<Invitation> invitations = new ArrayList<>();
        for (String studentId : studentEmails) {
            String uuid = UUID.randomUUID().toString();
            invitations.add(Invitation.of(uuid, studentId, courseId));
            sendInvitationEmail(uuid, studentId);
        }
        invitationRepository.saveAll(invitations);
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

            if (invitationRepository.existsById(invitation.getInvitationUuid())) {
                invitationRepository.delete(invitation);
            }
        } else {
            log.warn("Invitation not found: {}", inviteUUID);
            throw new InvitationNotFoundException();
        }
    }

    /**
     * Sends an invitation email to a student, which contains the correct link to accept the course invitation.
     * @param invitationUuid Id of the invitation
     * @param studentEmail Email address of the student
     */
    private void sendInvitationEmail(String invitationUuid, String studentEmail) {
        String subject = "Registration Confirmation - " + invitationConfig.getApplicationName();
        String confirmationUrl
                = invitationConfig.getApplicationURL() + "/api/course/acceptInvitation/" + invitationUuid;

        // This can be externalized via MessageSource, and get messages for different locales
        String message = "You can accept your course invitation via the following link: ";

        Mail email = new Mail();
        email.setTo(Collections.singletonList(studentEmail));
        email.setSubject(subject);
        email.setText(message + confirmationUrl);
        emailClient.sendMail(email);
    }
}
