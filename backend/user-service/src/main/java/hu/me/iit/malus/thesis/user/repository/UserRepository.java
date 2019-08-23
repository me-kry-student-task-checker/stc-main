package hu.me.iit.malus.thesis.user.repository;

import hu.me.iit.malus.thesis.user.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * /**
 * Spring Data repository, that handles all database operation for User objects
 * @author Javorek Dénes
 */
public interface UserRepository extends CrudRepository<User, String> {
}
