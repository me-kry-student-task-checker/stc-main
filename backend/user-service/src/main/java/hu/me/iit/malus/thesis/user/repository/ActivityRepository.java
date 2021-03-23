package hu.me.iit.malus.thesis.user.repository;

import hu.me.iit.malus.thesis.user.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository, that handles all db operations for Activity objects.
 *
 * @author Attila Szőke
 */
public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
