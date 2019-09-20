package hu.me.iit.malus.thesis.user.service.cron;

import hu.me.iit.malus.thesis.user.repository.ActivationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.time.Instant;

@Service
@Transactional
public class TokenPurgeTask {
    private ActivationTokenRepository tokenRepository;

    @Autowired
    public TokenPurgeTask(ActivationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Scheduled(cron = "${registration.token.purge.cron}")
    public void purgeExpiredTokens() {
        Date now = Date.from(Instant.now());
        tokenRepository.deleteAllExpiredSince(now);
    }
}