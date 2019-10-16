package hu.me.iit.malus.thesis.course.service.cron;

import hu.me.iit.malus.thesis.course.repository.InvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

/**
 * Cron job to delete expired invitations from database
 * @author Javorek Dénes
 */
@Service
@Transactional
public class InvitationPurgeTask {
    private InvitationRepository invitationRepository;

    @Autowired
    public InvitationPurgeTask(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    @Scheduled(cron = "${course.invitation.purge.cron}")
    public void purgeExpiredTokens() {
        Date now = Date.from(Instant.now());
        invitationRepository.deleteAllExpiredSince(now);
    }
}