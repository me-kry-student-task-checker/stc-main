package hu.me.iit.malus.thesis.user.service.impl;

import hu.me.iit.malus.thesis.user.model.Student;
import hu.me.iit.malus.thesis.user.model.Teacher;
import hu.me.iit.malus.thesis.user.model.User;
import hu.me.iit.malus.thesis.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository repository;
    private BCryptPasswordEncoder encoder;

    @Autowired
    public UserDetailsServiceImpl(UserRepository repository, BCryptPasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    /**
     * Purpose of this functional interface implementation, loads the user objects by username.
     * In our case the username equals to the user's email, because we dont use separate username field.
     * @param email
     * @return The corresponding UserDetails object, which holds the data of the give user
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if("admin@admin.com".equalsIgnoreCase(email)) {
            return new org.springframework.security.core.userdetails.User
                    (email, encoder.encode("admin123"), AuthorityUtils.createAuthorityList("ROLE_Admin"));
        }

        Optional<User> userToLoad = repository.findById(email);

        if(!userToLoad.isPresent()) {
            throw new UsernameNotFoundException("Username: " + email + " not found");
        }
        User user = userToLoad.get();

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.NO_AUTHORITIES;

        //TODO: This is disgusting, needs better role handling
        if (user instanceof Teacher) {
            grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_Teacher");
        } else if (user instanceof Student) {
            grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_Student");
        }

        return new org.springframework.security.core.userdetails.User
                (user.getEmail(), user.getPassword(), grantedAuthorities);
    }
}
