package hu.me.iit.malus.thesis.course.repository;

import hu.me.iit.malus.thesis.course.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, String> {
}
