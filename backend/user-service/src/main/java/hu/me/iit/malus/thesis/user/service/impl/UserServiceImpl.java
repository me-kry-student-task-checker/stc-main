package hu.me.iit.malus.thesis.user.service.impl;

import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;
import hu.me.iit.malus.thesis.user.model.*;
import hu.me.iit.malus.thesis.user.model.exception.EmailExistsException;
import hu.me.iit.malus.thesis.user.repository.*;
import hu.me.iit.malus.thesis.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Override
    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    @Override
    public void saveStudents(Set<Student> studentsToSave) {
        studentRepository.saveAll(studentsToSave);
    }

    @Override
    public void saveTeacher(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    @Override
    public Set<Student> getAllStudents() {
        return new HashSet<>(studentRepository.findAllBy());
    }

    @Override
    public Set<Teacher> getAllTeachers() {
        return new HashSet<>(teacherRepository.findAllBy());
    }

    @Override
    public Student getStudentByEmail(String studentEmail) {
        return studentRepository.findByEmail(studentEmail);
    }

    @Override
    public Teacher getTeacherByEmail(String teacherEmail) {
        return teacherRepository.findByEmail(teacherEmail);
    }

    /**
     * Checks whether a given email address already exists in our system, or not.
     * @param email To check
     * @return True if email already exists, false otherwise
     */
    private boolean emailExists(String email) {
        if (studentRepository.findByEmail(email) != null || teacherRepository.findByEmail(email) != null ||
                adminRepository.findByEmail(email) != null) {
            return true;
        } else {
            return false;
        }
    }
}
