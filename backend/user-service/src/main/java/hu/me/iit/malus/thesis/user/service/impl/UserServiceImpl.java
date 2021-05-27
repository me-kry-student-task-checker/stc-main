package hu.me.iit.malus.thesis.user.service.impl;

import hu.me.iit.malus.thesis.dto.Mail;
import hu.me.iit.malus.thesis.user.client.EmailClient;
import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;
import hu.me.iit.malus.thesis.user.controller.dto.StudentDto;
import hu.me.iit.malus.thesis.user.controller.dto.TeacherDto;
import hu.me.iit.malus.thesis.user.controller.dto.UserDto;
import hu.me.iit.malus.thesis.user.model.*;
import hu.me.iit.malus.thesis.user.model.exception.DatabaseOperationFailedException;
import hu.me.iit.malus.thesis.user.model.exception.EmailExistsException;
import hu.me.iit.malus.thesis.user.model.exception.UserNotFoundException;
import hu.me.iit.malus.thesis.user.model.factory.UserFactory;
import hu.me.iit.malus.thesis.user.repository.ActivationTokenRepository;
import hu.me.iit.malus.thesis.user.repository.AdminRepository;
import hu.me.iit.malus.thesis.user.repository.StudentRepository;
import hu.me.iit.malus.thesis.user.repository.TeacherRepository;
import hu.me.iit.malus.thesis.user.service.UserService;
import hu.me.iit.malus.thesis.user.service.converter.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.Stream;

/**
 * Default implementation of UserService.
 *
 * @author Javorek Dénes
 * @author Attila Szőke
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AdminRepository adminRepository;
    private final ActivationTokenRepository tokenRepository;
    private final EmailClient emailClient;
    private final UserFactory userFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public User registerNewUserAccount(RegistrationRequest registrationRequest) throws EmailExistsException {
        if (emailExists(registrationRequest.getEmail())) {
            throw new EmailExistsException(
                    "There is already an account with that email address: " + registrationRequest.getEmail());
        }
        var newUser = userFactory.create(registrationRequest);
        return saveUser(newUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createActivationToken(User user, String token) {
        final var userToken = ActivationToken.of(token, user);
        tokenRepository.save(userToken);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean activateUser(String token) {
        var activationTokenOpt = tokenRepository.findByToken(token);
        if (activationTokenOpt.isEmpty()) {
            return false;
        }
        var activationToken = activationTokenOpt.get();
        final var user = activationToken.getUser();
        final var cal = Calendar.getInstance();
        if ((activationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            tokenRepository.delete(activationToken);
            return false;
        }
        user.setEnabled(true);
        saveUser(user);
        tokenRepository.delete(activationToken);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    @Override
    public TeacherDto saveCourseCreation(String teacherEmail, Long courseId) {
        var teacher = teacherRepository.findLockByEmail(teacherEmail).orElseThrow(UserNotFoundException::new);
        try {
            teacher.getCreatedCourseIds().add(courseId);
            return Converter.createTeacherDtoFromTeacher(teacherRepository.save(teacher));
        } catch (DataAccessException e) {
            throw new DatabaseOperationFailedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
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
        var student = studentRepository.findByEmail(studentEmail).orElseThrow(() -> new UserNotFoundException(studentEmail));
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
        var teacher = teacherRepository.findByEmail(teacherEmail).orElseThrow(() -> new UserNotFoundException(teacherEmail));
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
        var teacher = teacherRepository.findByCreatedCourseId(courseId).orElseThrow(UserNotFoundException::new);
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
        var user = getAnyUserByEmail(userEmail);
        if (user instanceof Student) {
            relatedCourseIds.addAll(((Student) user).getAssignedCourseIds());
        }
        if (user instanceof Teacher) {
            relatedCourseIds.addAll(((Teacher) user).getCreatedCourseIds());
        }
        return relatedCourseIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRelatedToCourse(String email, Long courseId) {
        var user = getAnyUserByEmail(email);
        if (user instanceof Student) {
            return ((Student) user).getAssignedCourseIds().contains(courseId);
        }
        if (user instanceof Teacher) {
            return ((Teacher) user).getCreatedCourseIds().contains(courseId);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getAnyUserByEmail(String email) {
        return Stream.of(studentRepository.findByEmail(email), teacherRepository.findByEmail(email), adminRepository.findByEmail(email))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> {
                    throw new UserNotFoundException(email);
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDto getAnyUserDtoByEmail(String email) {
        var user = getAnyUserByEmail(email);
        if (user instanceof Student) {
            return Converter.createStudentDtoFromStudent((Student) user);
        }
        if (user instanceof Teacher) {
            return Converter.createTeacherDtoFromTeacher((Teacher) user);
        }
        if (user instanceof Admin) {
            return Converter.createAdminDtoFromStudent((Admin) user);
        }
        throw new IllegalStateException("User type cannot be recognized");
    }

    /**
     * Checks whether a given email address already exists in our system, or not.
     *
     * @param email To check
     * @return True if email already exists, false otherwise
     */
    private boolean emailExists(String email) {
        return studentRepository.findByEmail(email).isPresent()
                || teacherRepository.findByEmail(email).isPresent()
                || adminRepository.findByEmail(email).isPresent();
    }

    /**
     * Saves a user with the respective repository.
     *
     * @param user the user
     * @return the saved user
     */
    private User saveUser(User user) {
        switch (user.getRole()) {
            case ADMIN:
                return adminRepository.save((Admin) user);
            case TEACHER:
                return teacherRepository.save((Teacher) user);
            case STUDENT:
                return studentRepository.save((Student) user);
        }
        throw new IllegalStateException("User type cannot be recognized");
    }

    @Override
    @Transactional
    public void removeCourseIdFromRelatedLists(Long courseId) {
        List<Student> students = studentRepository.findAllAssignedForCourseId(courseId);
        students.forEach(student -> student.getAssignedCourseIds().remove(courseId));
        studentRepository.saveAll(students);
        Optional<Teacher> opt = teacherRepository.findByCreatedCourseId(courseId);
        if (opt.isPresent()) {
            Teacher teacher = opt.get();
            teacher.getCreatedCourseIds().remove(courseId);
            teacherRepository.save(teacher);
        }
    }
}
