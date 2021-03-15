package hu.me.iit.malus.thesis.user.service.impl;

import hu.me.iit.malus.thesis.user.model.User;
import hu.me.iit.malus.thesis.user.model.exception.UserNotFoundException;
import hu.me.iit.malus.thesis.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of a common Spring interface, used to load user data by its identifier during authentication.
 * @author Javorek Dénes
 */
@Slf4j
@Service
@Qualifier("customUserDetailsService")
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    /**
     * Functional interface implementation, loads the user objects by email.
     * In our case the email equals to the user's email, because we dont use separate email field.
     * @param email
     * @return The corresponding UserDetails object, which holds the data of the give user
     * @throws UsernameNotFoundException If the user cannot be found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // These flags are not defined in our system, but should be used for userdetails.User
        final boolean accountNonExpired = true;
        final boolean credentialsNonExpired = true;
        final boolean accountNonLocked = true;

        final User user;

        try {
            user = userService.getAnyUserByEmail(email);
        } catch (UserNotFoundException notFoundExc) {
            throw new UsernameNotFoundException("User with this email: " + notFoundExc.getEmail() + ", cannot be found");
        }

        List<GrantedAuthority> grantedAuthorities =
                AuthorityUtils.createAuthorityList(user.getRole().getRoleString());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                grantedAuthorities);
    }
}
