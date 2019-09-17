package hu.me.iit.malus.thesis.user.service.impl;

import hu.me.iit.malus.thesis.user.model.Student;
import hu.me.iit.malus.thesis.user.model.Teacher;
import hu.me.iit.malus.thesis.user.model.User;
import hu.me.iit.malus.thesis.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of a common Spring interface, used to load user data by its identifier during authentication.
 * @author Javorek Dénes
 */
@Service
@Qualifier("customUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository repository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Functional interface implementation, loads the user objects by email.
     * In our case the email equals to the user's email, because we dont use separate email field.
     * @param email
     * @return The corresponding UserDetails object, which holds the data of the give user
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // These flags are not defined in our system, but should be used for userdetails.User
        final boolean accountNonExpired = true;
        final boolean credentialsNonExpired = true;
        final boolean accountNonLocked = true;

        Optional<User> userToLoad = repository.findById(email);

        if(!userToLoad.isPresent()) {
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        }
        User user = userToLoad.get();

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
