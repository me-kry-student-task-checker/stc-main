package hu.me.iit.malus.thesis.user.repository;

import hu.me.iit.malus.thesis.user.model.ActivationToken;
import hu.me.iit.malus.thesis.user.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface ActivationTokenRepository extends CrudRepository<ActivationToken, String> {
    ActivationToken findByToken(String token);
    ActivationToken findByUser(User user);

    @Modifying
    @Query("DELETE FROM ActivationToken t WHERE t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date date);
}
