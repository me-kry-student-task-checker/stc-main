package hu.me.iit.malus.thesis.user.repository;

import hu.me.iit.malus.thesis.user.model.ActivationToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.Optional;

public interface ActivationTokenRepository extends CrudRepository<ActivationToken, String> {

    Optional<ActivationToken> findByToken(String token);

    @Modifying
    @Query("DELETE FROM ActivationToken t WHERE t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date date);
}
