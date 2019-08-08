package hu.me.iit.malus.thesis.email.model;

import org.springframework.web.multipart.MultipartFile;
import java.util.Collection;
import lombok.*;

/**
 * The type Mail.
 */

@Data @NoArgsConstructor
public class Mail {


    private String from;
    private String[] to;
    private String subject;
    private String[] ccs;
    private String[] bccs;
    private String text;
    private String replyTo;
    private Collection<MultipartFile> attachments;


    public Mail(String from, String[] to, String subject, String[] ccs, String[] bccs, String text, String replyTo, Collection<MultipartFile> attachments) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.ccs = ccs;
        this.bccs = bccs;
        this.text = text;
        this.replyTo = replyTo;
        this.attachments = attachments;
    }

    public Mail(String from, String[] to, String subject, String[] ccs, String[] bccs, String text, String replyTo) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.ccs = ccs;
        this.bccs = bccs;
        this.text = text;
        this.replyTo = replyTo;
        this.attachments = null;
    }

    public Mail(String from, String[] to, String subject, String[] ccs, String[] bccs, String text) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.ccs = ccs;
        this.bccs = bccs;
        this.text = text;
        this.replyTo = "";
        this.attachments = null;
    }

}
