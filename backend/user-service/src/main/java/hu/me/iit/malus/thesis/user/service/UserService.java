package hu.me.iit.malus.thesis.user.service;

import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;
import hu.me.iit.malus.thesis.user.model.Student;
import hu.me.iit.malus.thesis.user.model.Teacher;
import hu.me.iit.malus.thesis.user.model.User;
import hu.me.iit.malus.thesis.user.model.exception.EmailExistsException;

import java.util.Set;

/**
 * Defines all the necessary operations for User service implementations
 * @author Javorek Dénes
 */
public interface UserService {

    /**
     * Registers a new user into the database, throws exception if its email already registered
     * @param registrationRequest Contains all the required information for registration.
     * @return The registered User
     * @throws EmailExistsException If a user already exists with this email
     */
    User registerNewUserAccount(RegistrationRequest registrationRequest)
            throws EmailExistsException;

    /**
     * Creates and saves a new activation token for a user
     * @param user Owner of the token
     * @param token
     */
    void createActivationToken(User user, String token);

    /**
     * Activates a user by its activation token
     * @param token Sent by the user, from the activation email
     * @return True if the token is valid and the user is activated successfully,
     * false otherwise.
     */
    boolean activateUser(String token);

    /**
     * Saves a single Student
     * @param student
     */
    @Deprecated
    void saveStudent(Student student);

    /**
     * Saves a set of Students
     * @param studentsToAdd
     */
    @Deprecated
    void saveStudents(Set<Student> studentsToAdd);

    /**
     * Saves a single Teacher
     * @param teacher
     */
    @Deprecated
    void saveTeacher(Teacher teacher);

    /**
     * Saves a set of Teachers
     * @param teachersToAdd
     */
    @Deprecated
    void saveTeachers(Set<Teacher> teachersToAdd);

    /**
     * Adds the id of the newly created course to a teacher object
     * @param teacherEmail
     * @param courseId
     */
    void saveCourseCreation(String teacherEmail, Long courseId);

    /**
     * Returns all the saved Students
     * @return the students
     */
    Set<Student> getAllStudents();

    /**
     * Returns all the saved Teachers
     * @return the teachers
     */
    Set<Teacher> getAllTeachers();

    /**
     * Returns a single Student by its email (identifier)
     * @param studentEmail Student's email
     * @return The corresponding Student
     */
    Student getStudentByEmail(String studentEmail);

    /**
     * Returns all the Students who has been assigned to a course
     * @param courseId Id of a course, that the Students assigned to
     * @return The corresponding Students
     */
    Set<Student> getStudentsByAssignedCourseId(Long courseId);

    /**
     * Returns all the Students who is not already assigned to the given course
     * @param courseId Id of a course, that the Student NOT assigned to
     * @return The corresponding Students
     */
    Set<Student> getStudentsByNotAssignedCourseId(Long courseId);

    /**
     * Returns a single Teacher by its email (identifier)
     * @param teacherEmail Teacher's email
     * @return The corresponding Teacher
     */
    Teacher getTeacherByEmail(String teacherEmail);

    /**
     * Returns a single Teacher by a course id, that he owns
     * @param courseId Id of a course, that the Teacher has created before
     * @return The corresponding Teacher
     */
    Teacher getTeacherByCreatedCourseId(Long courseId);

    /**
     * Return a complete User object, queried by its email (identifier)
     * @param email of the User
     * @return User
     */
    User getAnyUserByEmail(String email);

    /**
     * Returns whether the user has any connection with the given course
     * This connection can be "created by" or "assigned to"
     * @param email Email address of the user
     * @param courseId Course id of the course where the connection should be tested
     * @return True if the user is connected to the course, false otherwise
     */
    Boolean isRelatedToCourse(String email, Long courseId);
}
