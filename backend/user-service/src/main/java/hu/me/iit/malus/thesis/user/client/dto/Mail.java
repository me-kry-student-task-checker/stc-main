package hu.me.iit.malus.thesis.user.client.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import java.util.List;

@Data
public class Mail {

    private List<@Email String> to;
    private String subject;
    private String[] ccs;
    private String[] bccs;
    private String text;
    private String replyTo;

    public Mail(List<@Email String> to, String subject, String text) {
        this.to = to;
        this.subject = subject;
        this.text = text;
    }
}
