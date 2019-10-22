package hu.me.iit.malus.thesis.user.repository;

import hu.me.iit.malus.thesis.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.LockModeType;
import java.util.Optional;


/**
 * Base Spring Data repository, that defines the common db operations for all User objects
 * @author Javorek Dénes
 */
@NoRepositoryBean
public interface UserBaseRepository<T extends User> extends JpaRepository<User, String> {
    Optional<T> findByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    //@QueryHints({@QueryHint(name = "javax.persistence.lock.scope", value   = "EXTENDED")})
    Optional<T> findLockByEmail(String email);

    boolean existsByEmail(String email);
}
