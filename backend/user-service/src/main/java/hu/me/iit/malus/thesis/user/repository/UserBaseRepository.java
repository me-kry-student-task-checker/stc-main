package hu.me.iit.malus.thesis.user.repository;

import hu.me.iit.malus.thesis.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.LockModeType;
import java.util.Optional;
import java.util.Set;


/**
 * Base Spring Data repository, that defines the common db operations for all User objects
 * @author Javorek Dénes
 */
@NoRepositoryBean
public interface UserBaseRepository<T extends User> extends JpaRepository<User, String> {
    @Query("SELECT u FROM #{#entityName} u")
    Set<T> findAllUsers();

    Optional<T> findByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<T> findLockByEmail(String email);

    boolean existsByEmail(String email);
}
