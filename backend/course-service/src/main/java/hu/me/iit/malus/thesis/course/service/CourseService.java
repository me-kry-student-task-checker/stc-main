package hu.me.iit.malus.thesis.course.service;

import hu.me.iit.malus.thesis.course.model.Course;
import hu.me.iit.malus.thesis.course.service.exception.CourseNotFoundException;
import hu.me.iit.malus.thesis.course.service.exception.InvitationNotFoundException;

import java.util.List;

/**
 * Interface of the Course Service
 * Defines all the possible operations for the service
 *
 * @author Javorek Dénes
 * @author Attila Szőke
 */
public interface CourseService {

    /**
     * Saves a course to the database
     *
     * @param course the new course
     * @return the saved course
     */
    Course create(Course course);

    /**
     * Saves an existing course with new parameters
     *
     * @param course the modified course which will be saved
     * @return the saved course
     */
    Course edit(Course course);

    /**
     * Gets a course by it's id
     *
     * @param courseId the id of the course
     * @return the course
     */
    Course get(Long courseId) throws CourseNotFoundException;

    //TODO might need to use pagination

    /**
     * Lists all courses
     *
     * @return list of courses
     */
    Iterable<Course> getAll();

    //TODO these will need a table which contains the uuid, a courseId and the studentId

    /**
     * Sends an invitation email to a student, which if accepted assigns a student to a course
     *
     * @param studentEmail the assignees email
     * @param courseId  the courses id
     * @return the UUID of the invitation
     */
    void invite(Long courseId, String studentEmail);

    /**
     * Sends multiple invitation e-mails to multiple students
     *
     * @param studentEmails the assignees email
     * @param courseId  the courses id
     * @return the UUID of the invitation
     */
    void invite(Long courseId, List<String> studentEmails);

    /**
     * Based on the UUID assigns a student to a course
     *
     * @param inviteUUID the UUID by which the student is assigned
     */
    void acceptInvite(String inviteUUID) throws InvitationNotFoundException;

}
