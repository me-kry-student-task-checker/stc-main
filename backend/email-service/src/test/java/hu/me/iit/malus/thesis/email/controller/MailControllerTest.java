package hu.me.iit.malus.thesis.email.controller;

import hu.me.iit.malus.thesis.email.controller.dto.MailDto;
import hu.me.iit.malus.thesis.email.service.MailService;
import hu.me.iit.malus.thesis.email.service.exception.MailCouldNotBeSentException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.Email;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class MailControllerTest {

    private final List<@Email String> testTo = List.of("Q1G", "11835h", "G6536");
    private final String testSubject = "kU5XaCO";
    private final String[] testCcs = {"DAu4", "46J3MXOv", "gv59"};
    private final String[] testBccs = {"5oL9", "ns360R0K", "1OVU"};
    private final String testText = "tdKdty";
    private final String testReplyTo = "WzJS";
    private final MailDto dto = new MailDto(
            testTo, testSubject, testCcs, testBccs, testText, testReplyTo
    );
    @Captor
    private ArgumentCaptor<MailDto> argumentCaptor;
    @Mock
    private MailService service;
    @InjectMocks
    private MailController controller;

    @Test
    public void send_noException_okResponse() throws MailCouldNotBeSentException {
        doNothing().when(service).sendEmail(any());
        ResponseEntity<Void> resp = controller.send(dto);
        verify(service).sendEmail(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), is(dto));
        assertThat(resp.getStatusCode(), is(HttpStatus.OK));
    }

    @Test(expected = MailCouldNotBeSentException.class)
    public void send_exceptionThrownAndHandled() throws MailCouldNotBeSentException {
        String testMessage = "vYoL";
        var cause = new Exception(testMessage);
        var e = new MailCouldNotBeSentException(cause);
        doThrow(e).when(service).sendEmail(any());
        controller.send(dto);
    }
}