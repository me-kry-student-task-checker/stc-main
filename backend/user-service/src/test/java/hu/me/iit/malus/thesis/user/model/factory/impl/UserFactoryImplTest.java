package hu.me.iit.malus.thesis.user.model.factory.impl;

import java.util.List;
import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;
import hu.me.iit.malus.thesis.user.model.User;
import hu.me.iit.malus.thesis.user.model.UserRole;
import hu.me.iit.malus.thesis.user.model.factory.UserFactory;
import hu.me.iit.malus.thesis.user.repository.AdminRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class UserFactoryImplTest {
    private BCryptPasswordEncoder passwordEncoder;
    private AdminRepository adminRepository;
    private UserFactory userFactory;

    @Before
    public void setUp() throws Exception {
        this.passwordEncoder = mock(BCryptPasswordEncoder.class);
        this.adminRepository = mock(AdminRepository.class);
        this.userFactory = new UserFactoryImpl(passwordEncoder, adminRepository);
    }

    @Test
    public void create() {
        // GIVEN
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail("user@example.com");
        registrationRequest.setFirstName("First");
        registrationRequest.setLastName("Last");
        registrationRequest.setPassword("example");
        registrationRequest.setRole(UserRole.STUDENT.getRoleString());

        when(passwordEncoder.encode(any())).thenReturn("1234");

        // WHEN
        User user = userFactory.create(registrationRequest);

        // THEN
        assertThat(user, is(notNullValue()));
    }

}
