package hu.me.iit.malus.thesis.course.service;

import hu.me.iit.malus.thesis.course.controller.dto.CourseCreateDto;
import hu.me.iit.malus.thesis.course.controller.dto.CourseFullDetailsDto;
import hu.me.iit.malus.thesis.course.controller.dto.CourseModificationDto;
import hu.me.iit.malus.thesis.course.controller.dto.CourseOverviewDto;
import hu.me.iit.malus.thesis.course.service.exception.CourseDeleteRollbackException;
import hu.me.iit.malus.thesis.course.service.exception.CourseNotFoundException;
import hu.me.iit.malus.thesis.course.service.exception.ForbiddenCourseEditException;

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
     * @param dto           the new course
     * @param creatorsEmail the email address of the creator
     * @return the saved course
     */
    CourseOverviewDto create(CourseCreateDto dto, String creatorsEmail);

    /**
     * Saves an existing course with new parameters
     *
     * @param dto          the modified course which will be saved
     * @param editorsEmail the email address of the editor
     * @return the saved course
     * @throws ForbiddenCourseEditException the forbidden course edit exception
     * @throws CourseNotFoundException      the course not found exception
     */
    CourseOverviewDto edit(CourseModificationDto dto, String editorsEmail) throws ForbiddenCourseEditException, CourseNotFoundException;

    /**
     * Gets a course by it's id
     *
     * @param courseId the id of the course
     * @return the course
     * @throws CourseNotFoundException the course not found exception
     */
    CourseFullDetailsDto get(Long courseId) throws CourseNotFoundException;

    /**
     * Lists all related courses, but only the most important infos.
     *
     * @param userEmail currently authenticated user's email
     * @return set of courses that relates to the user
     */
    Set<CourseOverviewDto> getAll(String userEmail);

    /**
     * Deletes a course, and everything connected to it using 2PC.
     *
     * @param courseId the course id
     * @throws CourseNotFoundException       the course not found exception
     * @throws ForbiddenCourseEditException  the forbidden course edit exception
     * @throws CourseDeleteRollbackException the course delete rollback exception
     */
    void deleteCourse(Long courseId) throws CourseNotFoundException, ForbiddenCourseEditException, CourseDeleteRollbackException;
}
