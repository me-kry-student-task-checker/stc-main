package hu.me.iit.malus.thesis.user.service.impl;

import hu.me.iit.malus.thesis.user.client.EmailClient;
import hu.me.iit.malus.thesis.user.client.dto.Mail;
import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;
import hu.me.iit.malus.thesis.user.model.*;
import hu.me.iit.malus.thesis.user.model.exception.DatabaseOperationFailedException;
import hu.me.iit.malus.thesis.user.model.exception.EmailExistsException;
import hu.me.iit.malus.thesis.user.model.exception.UserNotFoundException;
import hu.me.iit.malus.thesis.user.repository.*;
import hu.me.iit.malus.thesis.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Default implementation of UserService
 *
 * @author Javorek Dénes
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AdminRepository adminRepository;
    private final ActivationTokenRepository tokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailClient emailClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public User registerNewUserAccount(RegistrationRequest registrationRequest) throws EmailExistsException {
        if (this.emailExists(registrationRequest.getEmail())) {
            throw new EmailExistsException(
                    "There is already an account with that email address: " + registrationRequest.getEmail());
        }

        UserRole userRole = UserRole.fromString(registrationRequest.getRole());
        switch (userRole) {
            case ADMIN: {
                Admin newAdmin = new Admin(registrationRequest.getEmail(), this.passwordEncoder.encode(registrationRequest.getPassword()),
                        registrationRequest.getFirstName(), registrationRequest.getLastName());
                return this.adminRepository.save(newAdmin);
            }
            case TEACHER: {
                Teacher newTeacher = new Teacher(registrationRequest.getEmail(), this.passwordEncoder.encode(registrationRequest.getPassword()),
                        registrationRequest.getFirstName(), registrationRequest.getLastName(), Collections.emptyList());
                return this.teacherRepository.save(newTeacher);
            }
            case STUDENT: {
                Student newStudent = new Student(registrationRequest.getEmail(), this.passwordEncoder.encode(registrationRequest.getPassword()),
                        registrationRequest.getFirstName(), registrationRequest.getLastName(), Collections.emptyList());
                return this.studentRepository.save(newStudent);
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
        this.tokenRepository.save(userToken);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean activateUser(String token) {
        final ActivationToken activationToken = this.tokenRepository.findByToken(token);
        if (activationToken == null) {
            return false;
        }

        final User user = activationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((activationToken.getExpiryDate()
                .getTime()
                - cal.getTime()
                .getTime()) <= 0) {
            this.tokenRepository.delete(activationToken);
            return false;
        }

        UserBaseRepository userRepository = null;

        switch (user.getRole()) {
            case STUDENT: {
                userRepository = this.studentRepository;
                break;
            }
            case TEACHER: {
                userRepository = this.teacherRepository;
                break;
            }
            case ADMIN: {
                userRepository = this.adminRepository;
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

        this.tokenRepository.delete(activationToken);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void saveCourseCreation(String teacherEmail, Long courseId) {
        Optional<Teacher> optionalTeacher = this.teacherRepository.findLockByEmail(teacherEmail);

        if (optionalTeacher.isPresent()) {
            try {
                Teacher teacher = optionalTeacher.get();
                teacher.getCreatedCourseIds().add(courseId);
                this.teacherRepository.save(teacher);
            } catch (DataAccessException e) {
                throw new DatabaseOperationFailedException(e);
            }
        } else {
            throw new UserNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void assignStudentsToCourse(Long courseId, List<String> studentEmails) {
        Mail mail = new Mail();
        mail.setTo(studentEmails);
        mail.setSubject("Course assignment notification");
        mail.setText("You have been assigned to a course.");
        this.studentRepository.findAllLockByEmailIn(studentEmails).forEach(student -> {
            try {
                student.getAssignedCourseIds().add(courseId);
                this.studentRepository.save(student);
            } catch (DataAccessException e) {
                throw new DatabaseOperationFailedException(e);
            }
        });
        this.emailClient.sendMail(mail);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Student> getAllStudents() {
        try {
            return new HashSet<>(this.studentRepository.findAll());
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
            return new HashSet<>(this.teacherRepository.findAll());
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
            Optional<Student> optionalStudent = this.studentRepository.findByEmail(studentEmail);
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
            return new HashSet<>(this.studentRepository.findAllAssignedForCourseId(courseId));
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
            return new HashSet<>(this.studentRepository.findAllNotAssignedForCourseId(courseId));
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
            Optional<Teacher> optionalTeacher = this.teacherRepository.findByEmail(teacherEmail);
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
            Optional<Teacher> optTeacher = this.teacherRepository.findByCreatedCourseId(courseId);
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
        Optional<? extends User> userToLoad = this.studentRepository.findByEmail(email);
        if(!userToLoad.isPresent()) {
            userToLoad = this.teacherRepository.findByEmail(email);
        }
        if(!userToLoad.isPresent()) {
            userToLoad = this.adminRepository.findByEmail(email);
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
    public Set<Long> getRelatedCourseIds(String userEmail) {
        Set<Long> relatedCourseIds = new HashSet<>();
        User user = this.getAnyUserByEmail(userEmail);

        if (user instanceof Student) {
            relatedCourseIds.addAll(((Student) user).getAssignedCourseIds());
        } else if (user instanceof Teacher) {
            relatedCourseIds.addAll(((Teacher) user).getCreatedCourseIds());
        }
        return relatedCourseIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isRelatedToCourse(String email, Long courseId) {
        Optional<? extends User> optionalUser = this.studentRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            if(((Student) optionalUser.get()).getAssignedCourseIds().contains(courseId)) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }

        optionalUser = this.teacherRepository.findByEmail(email);
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
        return this.studentRepository.findByEmail(email).isPresent() || this.teacherRepository.findByEmail(email).isPresent() ||
                this.adminRepository.findByEmail(email).isPresent();
    }

}
