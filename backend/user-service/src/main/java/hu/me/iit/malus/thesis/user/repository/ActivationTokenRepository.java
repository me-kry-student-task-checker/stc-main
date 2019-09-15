package hu.me.iit.malus.thesis.user.repository;

import hu.me.iit.malus.thesis.user.model.ActivationToken;
import hu.me.iit.malus.thesis.user.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.stream.Stream;

public interface ActivationTokenRepository extends CrudRepository<ActivationToken, String> {
    ActivationToken findByToken(String token);

    ActivationToken findByUser(User user);

    Stream<ActivationToken> findAllByExpiryDateLessThan(Date now);

    void deleteByExpiryDateLessThan(Date now);

    @Modifying
    @Query("DELETE FROM ActivationToken t WHERE t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);
}
