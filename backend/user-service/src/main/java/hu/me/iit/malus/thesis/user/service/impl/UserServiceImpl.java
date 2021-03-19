package hu.me.iit.malus.thesis.user.service.impl;

import hu.me.iit.malus.thesis.user.client.EmailClient;
import hu.me.iit.malus.thesis.user.client.dto.Mail;
import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;
import hu.me.iit.malus.thesis.user.controller.dto.StudentDto;
import hu.me.iit.malus.thesis.user.controller.dto.TeacherDto;
import hu.me.iit.malus.thesis.user.controller.dto.UserDto;
import hu.me.iit.malus.thesis.user.model.*;
import hu.me.iit.malus.thesis.user.model.exception.DatabaseOperationFailedException;
import hu.me.iit.malus.thesis.user.model.exception.EmailExistsException;
import hu.me.iit.malus.thesis.user.model.exception.UserNotFoundException;
import hu.me.iit.malus.thesis.user.repository.*;
import hu.me.iit.malus.thesis.user.service.UserService;
import hu.me.iit.malus.thesis.user.service.converter.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
                        registrationRequest.getFirstName(), registrationRequest.getLastName(), Collections.emptyList());
                return teacherRepository.save(newTeacher);
            }
            case STUDENT: {
                Student newStudent = new Student(registrationRequest.getEmail(), passwordEncoder.encode(registrationRequest.getPassword()),
                        registrationRequest.getFirstName(), registrationRequest.getLastName(), Collections.emptyList());
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
    @Transactional
    @Override
    public void saveCourseCreation(String teacherEmail, Long courseId) {
        Teacher teacher = teacherRepository.findLockByEmail(teacherEmail).orElseThrow(UserNotFoundException::new);
        try {
            teacher.getCreatedCourseIds().add(courseId);
            teacherRepository.save(teacher);
        } catch (DataAccessException e) {
            throw new DatabaseOperationFailedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void assignStudentsToCourse(Long courseId, List<String> studentEmails) {
        studentRepository.findAllLockByEmailIn(studentEmails).forEach(student -> {
            try {
                student.getAssignedCourseIds().add(courseId);
                studentRepository.save(student);
            } catch (DataAccessException e) {
                throw new DatabaseOperationFailedException(e);
            }
        });
        emailClient.sendMail(new Mail(studentEmails, "Course assignment notification", "You have been assigned to a course."));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<StudentDto> getAllStudents() {
        try {
            return studentRepository
                    .findAll()
                    .stream()
                    .map(Converter::createStudentDtoFromStudent)
                    .collect(Collectors.toSet());
        } catch (DataAccessException e) {
            throw new DatabaseOperationFailedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<TeacherDto> getAllTeachers() {
        try {
            return teacherRepository
                    .findAll()
                    .stream()
                    .map(Converter::createTeacherDtoFromTeacher)
                    .collect(Collectors.toSet());
        } catch (DataAccessException e) {
            throw new DatabaseOperationFailedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentDto getStudentByEmail(String studentEmail) {
        Student student = studentRepository.findByEmail(studentEmail).orElseThrow(() -> new UserNotFoundException(studentEmail));
        try {
            return Converter.createStudentDtoFromStudent(student);
        } catch (DataAccessException dataExc) {
            throw new DatabaseOperationFailedException(dataExc);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<StudentDto> getStudentsByAssignedCourseId(Long courseId) {
        try {
            return studentRepository.findAllAssignedForCourseId(courseId)
                    .stream()
                    .map(Converter::createStudentDtoFromStudent)
                    .collect(Collectors.toSet());
        } catch (DataAccessException e) {
            throw new DatabaseOperationFailedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<StudentDto> getStudentsByNotAssignedCourseId(Long courseId) {
        try {
            return studentRepository.findAllNotAssignedForCourseId(courseId)
                    .stream()
                    .map(Converter::createStudentDtoFromStudent)
                    .collect(Collectors.toSet());
        } catch (DataAccessException e) {
            throw new DatabaseOperationFailedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeacherDto getTeacherByEmail(String teacherEmail) {
        Teacher teacher = teacherRepository.findByEmail(teacherEmail).orElseThrow(() -> new UserNotFoundException(teacherEmail));
        try {
            return Converter.createTeacherDtoFromTeacher(teacher);
        } catch (DataAccessException e) {
            throw new DatabaseOperationFailedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeacherDto getTeacherByCreatedCourseId(Long courseId) {
        Teacher teacher = teacherRepository.findByCreatedCourseId(courseId).orElseThrow(UserNotFoundException::new);
        try {
            return Converter.createTeacherDtoFromTeacher(teacher);
        } catch (DataAccessException e) {
            throw new DatabaseOperationFailedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Long> getRelatedCourseIds(String userEmail) {
        Set<Long> relatedCourseIds = new HashSet<>();
        User user = getAnyUserByEmail(userEmail);

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
    public User getAnyUserByEmail(String email) {
        // TODO: Looks ugly, there are better options for Optional chaining, maybe try those later
        Optional<? extends User> userToLoad = studentRepository.findByEmail(email);
        if (userToLoad.isEmpty()) {
            userToLoad = teacherRepository.findByEmail(email);
        }
        if (userToLoad.isEmpty()) {
            userToLoad = adminRepository.findByEmail(email);
        }

        if (userToLoad.isEmpty()) {
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
            if (((Student) optionalUser.get()).getAssignedCourseIds().contains(courseId)) {
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
     *
     * @param email To check
     * @return True if email already exists, false otherwise
     */
    private boolean emailExists(String email) {
        return studentRepository.findByEmail(email).isPresent() || teacherRepository.findByEmail(email).isPresent() ||
                adminRepository.findByEmail(email).isPresent();
    }

    /**
     * {@inheritDoc}
     */
    public UserDto getDtoFromAnyUser(User user) {
        if (user instanceof Student) {
            return Converter.createStudentDtoFromStudent((Student) user);
        } else {
            return Converter.createTeacherDtoFromTeacher((Teacher) user);
        }
    }

}
