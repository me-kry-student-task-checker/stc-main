package hu.me.iit.malus.thesis.course.repository;

import hu.me.iit.malus.thesis.course.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface InvitationRepository extends JpaRepository<Invitation, String> {
    @Modifying
    @Query("DELETE FROM Invitation i WHERE i.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);
}
