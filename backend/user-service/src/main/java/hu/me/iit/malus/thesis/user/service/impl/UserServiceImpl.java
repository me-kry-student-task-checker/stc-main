package hu.me.iit.malus.thesis.user.service.impl;

import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;
import hu.me.iit.malus.thesis.user.model.*;
import hu.me.iit.malus.thesis.user.model.exception.DatabaseOperationFailedException;
import hu.me.iit.malus.thesis.user.model.exception.EmailExistsException;
import hu.me.iit.malus.thesis.user.model.exception.IllegalUserInsertionException;
import hu.me.iit.malus.thesis.user.model.exception.UserNotFoundException;
import hu.me.iit.malus.thesis.user.repository.ActivationTokenRepository;
import hu.me.iit.malus.thesis.user.repository.AdminRepository;
import hu.me.iit.malus.thesis.user.repository.StudentRepository;
import hu.me.iit.malus.thesis.user.repository.TeacherRepository;
import hu.me.iit.malus.thesis.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

/**
 * Default implementation of UserService
 * @author Javorek Dénes
 */
@Service
public class UserServiceImpl implements UserService {
    private StudentRepository studentRepository;
    private TeacherRepository teacherRepository;
    private AdminRepository adminRepository;
    private ActivationTokenRepository tokenRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(StudentRepository studentRepository, TeacherRepository teacherRepository,
                           AdminRepository adminRepository, ActivationTokenRepository tokenRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.adminRepository = adminRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User registerNewUserAccount(RegistrationRequest registrationRequest) throws EmailExistsException {
        if (emailExists(registrationRequest.getEmail())) {
            throw new EmailExistsException(
                    "There is already an account with that email address: " + registrationRequest.getEmail());
        }

        UserRole userRole = UserRole.fromString(registrationRequest.getRole());
        switch (userRole) {
            case ADMIN: {
                Admin newAdmin = new Admin(registrationRequest.getEmail(), passwordEncoder.encode(registrationRequest.getPassword()),
                        registrationRequest.getFirstName(), registrationRequest.getLastName());
                return adminRepository.save(newAdmin);
            }
            case TEACHER: {
                Teacher newTeacher = new Teacher(registrationRequest.getEmail(), passwordEncoder.encode(registrationRequest.getPassword()),
                        registrationRequest.getFirstName(), registrationRequest.getLastName(), Collections.EMPTY_LIST);
                return teacherRepository.save(newTeacher);
            }
            case STUDENT: {
                Student newStudent = new Student(registrationRequest.getEmail(), passwordEncoder.encode(registrationRequest.getPassword()),
                        registrationRequest.getFirstName(), registrationRequest.getLastName(), Collections.EMPTY_LIST);
                return studentRepository.save(newStudent);
            }
            default:
                throw new IllegalStateException("User type cannot be recognized");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createActivationToken(User user, String token) {
        final ActivationToken userToken = ActivationToken.of(token, user);
        tokenRepository.save(userToken);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean activateUser(String token) {
        final ActivationToken activationToken = tokenRepository.findByToken(token);
        if (activationToken == null) {
            return false;
        }

        final User user = activationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((activationToken.getExpiryDate()
                .getTime()
                - cal.getTime()
                .getTime()) <= 0) {
            tokenRepository.delete(activationToken);
            return false;
        }

        switch (user.getRole()) {
            case STUDENT: {
                Student studentToActivate = studentRepository.findByEmail(user.getEmail());
                studentToActivate.setEnabled(true);
                studentRepository.save(studentToActivate);
                break;
            }
            case TEACHER: {
                Teacher teacherToActivate = teacherRepository.findByEmail(user.getEmail());
                teacherToActivate.setEnabled(true);
                teacherRepository.save(teacherToActivate);
                break;
            }
            case ADMIN: {
                Admin adminToActivate = adminRepository.findByEmail(user.getEmail());
                adminToActivate.setEnabled(true);
                adminRepository.save(adminToActivate);
                break;
            }
        }
        tokenRepository.delete(activationToken);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            try {
                student.setPassword(studentRepository.findByEmail(student.getEmail()).getPassword());
                studentRepository.save(student);
            } catch (DataAccessException e) {
                throw new DatabaseOperationFailedException(e);
            }
        } else {
            throw new IllegalUserInsertionException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveStudents(Set<Student> studentsToSave) {
        for (Student student : studentsToSave) {
            if (!studentRepository.existsByEmail(student.getEmail())) {
                throw new IllegalUserInsertionException();
            }
            student.setPassword(studentRepository.findByEmail(student.getEmail()).getPassword());
        }

        try {
            studentRepository.saveAll(studentsToSave);
        } catch (DataAccessException e) {
            throw new DatabaseOperationFailedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveTeacher(Teacher teacher) {
        if (teacherRepository.existsByEmail(teacher.getEmail())) {
            try {
                teacher.setPassword(teacherRepository.findByEmail(teacher.getEmail()).getPassword());
                teacherRepository.save(teacher);
            } catch (DataAccessException e) {
                throw new DatabaseOperationFailedException(e);
            }
        } else {
            throw new IllegalUserInsertionException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveTeachers(Set<Teacher> teachersToSave) {
        for (Teacher teacher : teachersToSave) {
            if (!teacherRepository.existsByEmail(teacher.getEmail())) {
                throw new IllegalUserInsertionException();
            }
            teacher.setPassword(teacherRepository.findByEmail(teacher.getEmail()).getPassword());
        }

        try {
            teacherRepository.saveAll(teachersToSave);
        } catch (DataAccessException e) {
            throw new DatabaseOperationFailedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Student> getAllStudents() {
        try {
            return new HashSet<>(studentRepository.findAllBy());
        } catch (DataAccessException e) {
            throw new DatabaseOperationFailedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Teacher> getAllTeachers() {
        try {
            return new HashSet<>(teacherRepository.findAllBy());
        } catch (DataAccessException e) {
            throw new DatabaseOperationFailedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Student getStudentByEmail(String studentEmail) {
        try {
            return  studentRepository.findByEmail(studentEmail);
        } catch (EntityNotFoundException notFoundExc) {
            throw new UserNotFoundException(studentEmail);
        } catch (DataAccessException dataExc) {
            throw new DatabaseOperationFailedException(dataExc);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Student> getStudentsByAssignedCourseId(Long courseId) {
        try {
            return new HashSet<>(studentRepository.findAllAssignedForCourseId(courseId));
        } catch (DataAccessException e) {
            throw new DatabaseOperationFailedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Student> getStudentsByNotAssignedCourseId(Long courseId) {
        try {
            return new HashSet<>(studentRepository.findAllNotAssignedForCourseId(courseId));
        } catch (DataAccessException e) {
            throw new DatabaseOperationFailedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Teacher getTeacherByEmail(String teacherEmail) {
        try {
            return  teacherRepository.findByEmail(teacherEmail);
        } catch (EntityNotFoundException notFoundExc) {
            throw new UserNotFoundException(teacherEmail);
        }  catch (DataAccessException e) {
            throw new DatabaseOperationFailedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Teacher getTeacherByCreatedCourseId(Long courseId) {
        try {
            Optional<Teacher> optTeacher = teacherRepository.findByCreatedCourseId(courseId);
            if (!optTeacher.isPresent()) {
                throw new UserNotFoundException();
            }
            return optTeacher.get();
        } catch (DataAccessException e) {
            throw new DatabaseOperationFailedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getAnyUserByEmail(String email) {
        // TODO: Looks ugly, there are better options for Optional chaining, maybe try those later
        Optional<User> userToLoad = Optional.ofNullable(studentRepository.findByEmail(email));
        if(!userToLoad.isPresent()) {
            userToLoad = Optional.ofNullable(teacherRepository.findByEmail(email));
        }
        if(!userToLoad.isPresent()) {
            userToLoad = Optional.ofNullable(adminRepository.findByEmail(email));
        }

        if(!userToLoad.isPresent()) {
            throw new UserNotFoundException(email);
        }
        return userToLoad.get();
    }

    /**
     * Checks whether a given email address already exists in our system, or not.
     * @param email To check
     * @return True if email already exists, false otherwise
     */
    private boolean emailExists(String email) {
        return studentRepository.findByEmail(email) != null || teacherRepository.findByEmail(email) != null ||
                adminRepository.findByEmail(email) != null;
    }

}
