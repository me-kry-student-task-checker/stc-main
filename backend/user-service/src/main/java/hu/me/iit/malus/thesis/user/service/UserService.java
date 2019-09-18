package hu.me.iit.malus.thesis.user.service;

import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;
import hu.me.iit.malus.thesis.user.model.Student;
import hu.me.iit.malus.thesis.user.model.Teacher;
import hu.me.iit.malus.thesis.user.model.exception.EmailExistsException;
import hu.me.iit.malus.thesis.user.model.User;

import java.util.Set;

/**
 * Defines all the necessary operations for User service implementations
 * @author Javorek Dénes
 */
public interface UserService {

    //Registration

    /**
     * Registers a new user into the database, throws exception if its email already registered
     * @param registrationRequest Contains all the required information for registration.
     * @return The registered User
     * @throws EmailExistsException
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

    //API

    /**
     * Saves a single
     * @param student
     */
    void saveStudent(Student student) throws IllegalArgumentException;

    void saveStudents(Set<Student> studentsToAdd) throws IllegalArgumentException;

    void saveTeacher(Teacher teacher) throws IllegalArgumentException;

    Set<Student> getAllStudents();

    Set<Teacher> getAllTeachers();

    Student getStudentByEmail(String studentEmail);

    Teacher getTeacherByEmail(String teacherEmail);
}
