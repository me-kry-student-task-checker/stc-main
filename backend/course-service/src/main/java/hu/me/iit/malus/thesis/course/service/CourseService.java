package hu.me.iit.malus.thesis.course.service;

import hu.me.iit.malus.thesis.course.model.Course;
import hu.me.iit.malus.thesis.course.service.exception.CourseNotFoundException;

import java.util.Set;

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
     * @param creatorsEmail the email address of the creator
     * @return the saved course
     */
    Course create(Course course, String creatorsEmail);

    /**
     * Saves an existing course with new parameters
     *
     * @param course the modified course which will be saved
     * @param editorsEmail the email address of the editor
     * @return the saved course
     */
    Course edit(Course course, String editorsEmail);

    /**
     * Gets a course by it's id
     *
     * @param courseId the id of the course
     * @param userEmail currently authenticated user's email
     * @return the course
     */
    Course get(Long courseId, String userEmail) throws CourseNotFoundException;

    /**
     * Lists all related courses, but only the most important infos.
     *
     * @param userEmail currently authenticated user's email
     * @return set of courses that relates to the user
     */
    Set<Course> getAll(String userEmail);
    
}
