package hu.me.iit.malus.thesis.user.repository;

import hu.me.iit.malus.thesis.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;



/**
 * Base Spring Data repository, that defines the common db operations for all User objects
 * @author Javorek Dénes
 */
@NoRepositoryBean
public interface UserBaseRepository<T extends User> extends JpaRepository<User, String> {
    T findByEmail(String email);
    boolean existsByEmail(String email);
}
