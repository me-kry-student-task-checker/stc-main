package hu.me.iit.malus.thesis.user.service.impl;

import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;
import hu.me.iit.malus.thesis.user.model.*;
import hu.me.iit.malus.thesis.user.model.exception.DatabaseOperationFailedException;
import hu.me.iit.malus.thesis.user.model.exception.EmailExistsException;
import hu.me.iit.malus.thesis.user.model.exception.IllegalUserInsertionException;
import hu.me.iit.malus.thesis.user.model.exception.UserNotFoundException;
import hu.me.iit.malus.thesis.user.repository.*;
import hu.me.iit.malus.thesis.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Default implementation of UserService
 * @author Javorek Dénes
 */
@Service
@Slf4j
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

        UserBaseRepository userRepository = null;

        switch (user.getRole()) {
            case STUDENT: {
                userRepository = studentRepository;
                break;
            }
            case TEACHER: {
                userRepository = teacherRepository;
                break;
            }
            case ADMIN: {
                userRepository = adminRepository;
                break;
            }
            default: {
                log.warn("User corresponding to token {}, does not have a valid role, cannot activate it.", token);
                return false;
            }
        }

        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if (optionalUser.isPresent())
        {
            User userToActivate = optionalUser.get();
            userToActivate.setEnabled(true);
            userRepository.save(userToActivate);
        }

        tokenRepository.delete(activationToken);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveStudent(Student studentToSave) {
        Optional<Student> optionalStudent = studentRepository.findByEmail(studentToSave.getEmail());
        if (optionalStudent.isPresent()) {
            try {
                studentToSave.setPassword(optionalStudent.get().getPassword());
                studentRepository.save(studentToSave);
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
            Optional<Student> optionalStudent = studentRepository.findByEmail(student.getEmail());

            if (!optionalStudent.isPresent()) {
                throw new IllegalUserInsertionException();
            }
            student.setPassword(optionalStudent.get().getPassword());
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
    public void saveTeacher(Teacher teacherToSave) {
        Optional<Teacher> optionalTeacher = teacherRepository.findByEmail(teacherToSave.getEmail());

        if (optionalTeacher.isPresent()) {
            try {
                teacherToSave.setPassword(optionalTeacher.get().getPassword());
                teacherRepository.save(teacherToSave);
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
            Optional<Teacher> optionalTeacher = teacherRepository.findByEmail(teacher.getEmail());

            if (!optionalTeacher.isPresent()) {
                throw new IllegalUserInsertionException();
            }
            teacher.setPassword(optionalTeacher.get().getPassword());
        }

        try {
            teacherRepository.saveAll(teachersToSave);
        } catch (DataAccessException e) {
            throw new DatabaseOperationFailedException(e);
        }
    }

    @Transactional
    @Override
    public void saveCourseCreation(String teacherEmail, Long courseId) {
        Optional<Teacher> optionalTeacher = teacherRepository.findLockByEmail(teacherEmail);

        if (optionalTeacher.isPresent()) {
            try {
                Teacher teacher = optionalTeacher.get();
                teacher.getCreatedCourseIds().add(courseId);
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
            Optional<Student> optionalStudent = studentRepository.findByEmail(studentEmail);
            if (!optionalStudent.isPresent()) throw new UserNotFoundException(studentEmail);
            return optionalStudent.get();
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
            Optional<Teacher> optionalTeacher = teacherRepository.findByEmail(teacherEmail);
            if (!optionalTeacher.isPresent()) throw new UserNotFoundException(teacherEmail);
            return optionalTeacher.get();
        } catch (DataAccessException e) {
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
        Optional<? extends User> userToLoad = studentRepository.findByEmail(email);
        if(!userToLoad.isPresent()) {
            userToLoad = teacherRepository.findByEmail(email);
        }
        if(!userToLoad.isPresent()) {
            userToLoad = adminRepository.findByEmail(email);
        }

        if(!userToLoad.isPresent()) {
            throw new UserNotFoundException(email);
        }
        return userToLoad.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isRelatedToCourse(String email, Long courseId) {
        Optional<? extends User> optionalUser = studentRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            if(((Student) optionalUser.get()).getAssignedCourseIds().contains(courseId)) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }

        optionalUser = teacherRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            if(((Teacher) optionalUser.get()).getCreatedCourseIds().contains(courseId)) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        return Boolean.FALSE;
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
