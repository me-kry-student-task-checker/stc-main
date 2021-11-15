package hu.me.iit.malus.thesis.user.service.cron;

import hu.me.iit.malus.thesis.user.repository.ActivationTokenRepository;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TokenPurgeTaskTest {

    @Test
    public void purgeExpiredTokens() {
        // GIVEN
        ActivationTokenRepository tokenRepository = mock(ActivationTokenRepository.class);
        TokenPurgeTask task = new TokenPurgeTask(tokenRepository);

        // WHEN
        task.purgeExpiredTokens();

        // THEN
        verify(tokenRepository, times(1)).deleteAllExpiredSince(any());
    }
}