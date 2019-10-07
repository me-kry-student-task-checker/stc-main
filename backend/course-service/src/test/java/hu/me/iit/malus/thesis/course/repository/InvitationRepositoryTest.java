package hu.me.iit.malus.thesis.course.repository;

import hu.me.iit.malus.thesis.course.model.Invitation;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Javorek Dénes
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class InvitationRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InvitationRepository repository;

    @Before
    public void reset() {
        entityManager.clear();
    }

    @Test
    public void whenFindById_invitationFound_wrappedInOptional() {
        // Given
        String invitationId = UUID.randomUUID().toString();

        Invitation invitation = new Invitation(
                invitationId, "invited@user.com", 1L);
        entityManager.persistAndFlush(invitation);

        // When
        Optional<Invitation> foundById = repository.findById(invitationId);

        // Then
        Assertions.assertThat(foundById.isPresent()).isEqualTo(true);
        Assertions.assertThat(foundById.get()).isEqualTo(invitation);
    }

    @Test
    public void whenSave_invitationExists() {
        // Given
        String invitationId = UUID.randomUUID().toString();

        Invitation invitation = new Invitation(
                invitationId, "invited@student.com", 2L);
        repository.save(invitation);

        // When
        Invitation foundById = entityManager.find(Invitation.class, invitationId);

        // Then
        Assertions.assertThat(foundById).isEqualTo(invitation);
    }
}
