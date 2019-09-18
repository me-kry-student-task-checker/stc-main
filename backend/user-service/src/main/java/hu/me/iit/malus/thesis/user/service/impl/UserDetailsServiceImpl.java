package hu.me.iit.malus.thesis.user.service.impl;

import hu.me.iit.malus.thesis.user.model.User;
import hu.me.iit.malus.thesis.user.repository.AdminRepository;
import hu.me.iit.malus.thesis.user.repository.StudentRepository;
import hu.me.iit.malus.thesis.user.repository.TeacherRepository;
import hu.me.iit.malus.thesis.user.repository.UserBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Implementation of a common Spring interface, used to load user data by its identifier during authentication.
 * @author Javorek Dénes
 */
@Service
@Qualifier("customUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private AdminRepository adminRepository;
    private TeacherRepository teacherRepository;
    private StudentRepository studentRepository;

    @Autowired
    public UserDetailsServiceImpl(AdminRepository adminRepository, TeacherRepository teacherRepository,
                                  StudentRepository studentRepository) {
        this.adminRepository = adminRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
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

        // Looks ugly, there are better options for Optional chaining, maybe try those later
        Optional<User> userToLoad = Optional.ofNullable(studentRepository.findByEmail(email));
        if(!userToLoad.isPresent()) {
            userToLoad = Optional.ofNullable(teacherRepository.findByEmail(email));
        }
        if(!userToLoad.isPresent()) {
            userToLoad = Optional.ofNullable(adminRepository.findByEmail(email));
        }

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
