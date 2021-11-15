package hu.me.iit.malus.thesis.user.service.impl;

import hu.me.iit.malus.thesis.user.model.UserRole;
import hu.me.iit.malus.thesis.user.model.Admin;
import hu.me.iit.malus.thesis.user.service.UserService;
import hu.me.iit.malus.thesis.user.service.exception.UserNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDetailsServiceImplTest {

    @Test(expected = UsernameNotFoundException.class)
    public void whenUserIsNotExists_loadUserByUsernameThrowsException() throws UserNotFoundException {
        // GIVEN
        String username = "feri";
        UserService userService = mock(UserService.class);
        UserDetailsService userDetailsService = new UserDetailsServiceImpl(userService);
        when(userService.getAnyUserByEmail(username)).thenThrow(UserNotFoundException.class);

        // WHEN
        userDetailsService.loadUserByUsername(username);

        // THEN
    }

    @Test
    public void whenUserIsExists_loadUserByUsernameReturnsUser() throws UserNotFoundException {
        // GIVEN
        String password = "feri";
        String firstName = "Lakatos";
        String lastName = "Ferenc";
        String email = "lakatos.ferenc@example.com";
        Admin admin = new Admin(email, password, firstName, lastName);

        UserService userService = mock(UserService.class);
        UserDetailsService userDetailsService = new UserDetailsServiceImpl(userService);
        when(userService.getAnyUserByEmail(email)).thenReturn(admin);

        // WHEN
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // THEN
        Assertions.assertThat(userDetails.getUsername()).isEqualTo(admin.getEmail());
        Assertions.assertThat(userDetails.getPassword()).isEqualTo(admin.getPassword());
        Assertions.assertThat(userDetails.isEnabled()).isEqualTo(admin.isEnabled());
        Assertions.assertThat(userDetails.isAccountNonExpired()).isTrue();
        Assertions.assertThat(userDetails.isCredentialsNonExpired()).isTrue();
        Assertions.assertThat(userDetails.isAccountNonLocked()).isTrue();
        Assertions.assertThat(userDetails.getAuthorities()).hasSize(1);
        Assertions.assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo(UserRole.ADMIN.getRoleString());
    }
}