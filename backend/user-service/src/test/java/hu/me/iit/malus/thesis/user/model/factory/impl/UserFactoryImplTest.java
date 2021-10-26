package hu.me.iit.malus.thesis.user.model.factory.impl;

import java.util.List;
import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;
import hu.me.iit.malus.thesis.user.model.User;
import hu.me.iit.malus.thesis.user.model.UserRole;
import hu.me.iit.malus.thesis.user.model.factory.UserFactory;
import hu.me.iit.malus.thesis.user.repository.AdminRepository;
import hu.me.iit.malus.thesis.user.service.exception.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class UserFactoryImplTest {
    private BCryptPasswordEncoder passwordEncoder;
    private AdminRepository adminRepository;
    private UserFactory userFactory;

    private RegistrationRequest registrationRequest;

    @Before
    public void setUp() throws Exception {
        this.passwordEncoder = mock(BCryptPasswordEncoder.class);
        this.adminRepository = mock(AdminRepository.class);
        this.userFactory = new UserFactoryImpl(passwordEncoder, adminRepository);
        this.registrationRequest = new RegistrationRequest();
        this.registrationRequest.setEmail("user@example.com");
        this.registrationRequest.setFirstName("First");
        this.registrationRequest.setLastName("Last");
        this.registrationRequest.setPassword("example");
        this.registrationRequest.setRole(UserRole.STUDENT.getRoleString());
    }

    @Test
    public void create() throws IllegalStateException {
        // GIVEN
        when(passwordEncoder.encode(any())).thenReturn("1234");

        // WHEN
        User user1 = userFactory.create(registrationRequest);

        registrationRequest.setRole(UserRole.TEACHER.getRoleString());
        User user2 = userFactory.create(registrationRequest);

        registrationRequest.setRole(UserRole.ADMIN.getRoleString());
        User user3 = userFactory.create(registrationRequest);

        // THEN
        assertThat(user1, is(notNullValue()));
        assertThat(user2, is(notNullValue()));
        assertThat(user3, is(notNullValue()));
    }

}
