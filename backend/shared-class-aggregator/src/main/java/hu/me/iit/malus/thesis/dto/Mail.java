package hu.me.iit.malus.thesis.dto;

import lombok.Data;

import java.util.List;

@Data
public class Mail {

    private List<String> to;
    private String subject;
    private String[] ccs;
    private String[] bccs;
    private String text;
    private String replyTo;

    public Mail(List<String> to, String subject, String text) {
        this.to = to;
        this.subject = subject;
        this.text = text;
    }
}
