package hu.me.iit.malus.thesis.email.controller;

import hu.me.iit.malus.thesis.email.service.exception.MailCouldNotBeSentException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class MailControllerAdviceTest {

    private final MailControllerAdvice mailControllerAdvice = new MailControllerAdvice();

    @Test
    public void handle_correctResponseOnException() {
        String testMsg = "zX9UXm0t";
        Exception testCause = new Exception(testMsg);
        MailCouldNotBeSentException testException = new MailCouldNotBeSentException(testCause);

        ResponseEntity<Map<String, String>> response = mailControllerAdvice.handle(testException);

        assertThat(response.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(response.getBody(), is(not(nullValue())));
        var body = response.getBody();
        assertTrue(body.containsKey(MailControllerAdvice.MSG));
        var msg = body.get(MailControllerAdvice.MSG);
        assertThat(msg, is(String.format(MailControllerAdvice.MSG_FORMAT, testException.getMessage(), testMsg)));
    }
}