package hu.me.iit.malus.thesis.email.service.impl;

import hu.me.iit.malus.thesis.email.controller.dto.MailDto;
import hu.me.iit.malus.thesis.email.service.exception.MailCouldNotBeSentException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.validation.constraints.Email;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MailServiceImplTest {

    private final List<@Email String> testTo = List.of("Q1G", "11835h", "G6536");
    private final String testSubject = "kU5XaCO";
    private final String[] testCcs = {"DAu4", "46J3MXOv", "gv59"};
    private final String[] testBccs = {"5oL9", "ns360R0K", "1OVU"};
    private final String testText = "tdKdty";
    private final String testReplyTo = "WzJS";

    @Captor
    private ArgumentCaptor<SimpleMailMessage> argumentCaptor;

    @Mock
    private JavaMailSender mockMailSender;

    @InjectMocks
    private MailServiceImpl service;

    private MailDto dto;
    private SimpleMailMessage mail;

    @Before
    public void setUp() {
        dto = new MailDto(testTo, testSubject, testCcs, testBccs, testText, testReplyTo);
        mail = new SimpleMailMessage();
        mail.setTo(dto.getTo().toArray(new String[0]));
        mail.setFrom(MailServiceImpl.FROM);
        mail.setReplyTo(dto.getReplyTo());
        mail.setCc(dto.getCcs());
        mail.setBcc(dto.getBccs());
        mail.setSubject(dto.getSubject());
        mail.setText(dto.getText());
    }

    @Test
    public void sendEmail_verifiedAndCorrectArguments() throws MailCouldNotBeSentException {
        doNothing().when(mockMailSender).send(any(SimpleMailMessage.class));
        service.sendEmail(dto);
        verify(mockMailSender).send(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), is(mail));
    }

    @Test(expected = MailCouldNotBeSentException.class)
    public void sendEmail_exceptionThrown() throws MailCouldNotBeSentException {
        doThrow(mock(MailException.class)).when(mockMailSender).send(any(SimpleMailMessage.class));
        service.sendEmail(dto);
        verify(mockMailSender).send(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), is(mail));
    }
}