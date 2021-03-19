package hu.me.iit.malus.thesis.user.service;

import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;
import hu.me.iit.malus.thesis.user.controller.dto.StudentDto;
import hu.me.iit.malus.thesis.user.controller.dto.TeacherDto;
import hu.me.iit.malus.thesis.user.controller.dto.UserDto;
import hu.me.iit.malus.thesis.user.model.User;
import hu.me.iit.malus.thesis.user.model.exception.EmailExistsException;

import java.util.List;
import java.util.Set;

/**
 * Defines all the necessary operations for User service implementations
 *
 * @author Javorek Dénes
 */
public interface UserService {

    /**
     * Registers a new user into the database, throws exception if its email already registered
     *
     * @param registrationRequest Contains all the required information for registration.
     * @return The registered User
     * @throws EmailExistsException If a user already exists with this email
     */
    User registerNewUserAccount(RegistrationRequest registrationRequest)
            throws EmailExistsException;

    /**
     * Creates and saves a new activation token for a user
     *
     * @param user  Owner of the token
     * @param token the token
     */
    void createActivationToken(User user, String token);

    /**
     * Activates a user by its activation token
     *
     * @param token Sent by the user, from the activation email
     * @return True if the token is valid and the user is activated successfully, false otherwise.
     */
    boolean activateUser(String token);

    /**
     * Adds the id of the newly created course to a teacher object
     *
     * @param teacherEmail the teacher email
     * @param courseId     the course id
     */
    void saveCourseCreation(String teacherEmail, Long courseId);

    /**
     * Adds the id of a course to the assigned the students.
     *
     * @param courseId      the course id
     * @param studentEmails the student emails
     */
    void assignStudentsToCourse(Long courseId, List<String> studentEmails);

    /**
     * Returns all the saved Students
     *
     * @return the students
     */
    Set<StudentDto> getAllStudents();

    /**
     * Returns all the saved Teachers
     *
     * @return the teachers
     */
    Set<TeacherDto> getAllTeachers();

    /**
     * Returns a single Student by its email (identifier)
     *
     * @param studentEmail Student's email
     * @return The corresponding Student
     */
    StudentDto getStudentByEmail(String studentEmail);

    /**
     * Returns all the Students who has been assigned to a course
     *
     * @param courseId Id of a course, that the Students assigned to
     * @return The corresponding Students
     */
    Set<StudentDto> getStudentsByAssignedCourseId(Long courseId);

    /**
     * Returns all the Students who is not already assigned to the given course
     *
     * @param courseId Id of a course, that the Student NOT assigned to
     * @return The corresponding Students
     */
    Set<StudentDto> getStudentsByNotAssignedCourseId(Long courseId);

    /**
     * Returns a single Teacher by its email (identifier)
     *
     * @param teacherEmail Teacher's email
     * @return The corresponding Teacher
     */
    TeacherDto getTeacherByEmail(String teacherEmail);

    /**
     * Returns a single Teacher by a course id, that he owns
     *
     * @param courseId Id of a course, that the Teacher has created before
     * @return The corresponding Teacher
     */
    TeacherDto getTeacherByCreatedCourseId(Long courseId);

    /**
     * Returns all the course ids, that the user has connection to
     * This connection can be "created by" or "assigned to"
     *
     * @param userEmail Identifier of a user
     * @return List of course ids, that is connected to the given user
     */
    Set<Long> getRelatedCourseIds(String userEmail);

    /**
     * Returns whether the user has any connection with the given course
     * This connection can be "created by" or "assigned to"
     *
     * @param email    Email address of the user
     * @param courseId Course id of the course where the connection should be tested
     * @return True if the user is connected to the course, false otherwise
     */
    Boolean isRelatedToCourse(String email, Long courseId);

    /**
     * Return a complete User object, queried by its email (identifier)
     *
     * @param email of the User
     * @return User any user by email
     */
    User getAnyUserByEmail(String email);

    /**
     * Gets a dto from user, for the controller to use.
     *
     * @param user the user
     * @return the dto
     */
    UserDto getDtoFromAnyUser(User user);
}
