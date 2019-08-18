package hu.me.iit.malus.thesis.course.repository;

import hu.me.iit.malus.thesis.course.model.Invitation;
import org.springframework.data.repository.CrudRepository;

public interface InvitationRepository extends CrudRepository<Invitation, String> {
}
