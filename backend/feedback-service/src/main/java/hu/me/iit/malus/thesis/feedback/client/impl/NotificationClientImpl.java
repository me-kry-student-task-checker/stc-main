package hu.me.iit.malus.thesis.feedback.client.impl;

import hu.me.iit.malus.thesis.feedback.client.NotificationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationClientImpl implements NotificationClient {

    @Override
    public void sendNotification() {
        throw new RuntimeException("This is a transaction test case so it always throws an exception!");
    }
}
