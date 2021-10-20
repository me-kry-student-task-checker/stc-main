package hu.me.iit.malus.thesis.user.service.impl;

import hu.me.iit.malus.thesis.user.client.EmailClient;
import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;
import hu.me.iit.malus.thesis.user.model.Student;
import hu.me.iit.malus.thesis.user.model.User;
import hu.me.iit.malus.thesis.user.model.UserRole;
import hu.me.iit.malus.thesis.user.model.factory.UserFactory;
import hu.me.iit.malus.thesis.user.repository.ActivationTokenRepository;
import hu.me.iit.malus.thesis.user.repository.AdminRepository;
import hu.me.iit.malus.thesis.user.repository.StudentRepository;
import hu.me.iit.malus.thesis.user.repository.TeacherRepository;
import hu.me.iit.malus.thesis.user.service.UserService;
import hu.me.iit.malus.thesis.user.service.exception.EmailExistsException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    private StudentRepository studentRepository;
    private TeacherRepository teacherRepository;
    private AdminRepository adminRepository;
    private ActivationTokenRepository tokenRepository;
    private EmailClient emailClient;
    private UserFactory userFactory;

    private UserService userService;

    @Before
    public void setUp() throws Exception {
        this.studentRepository = mock(StudentRepository.class);
        this.teacherRepository = mock(TeacherRepository.class);
        this.adminRepository = mock(AdminRepository.class);
        this.tokenRepository = mock(ActivationTokenRepository.class);
        this.emailClient = mock(EmailClient.class);
        this.userFactory = mock(UserFactory.class);
        this.userService = new UserServiceImpl(studentRepository, teacherRepository, adminRepository,
                tokenRepository, emailClient, userFactory);
    }

}