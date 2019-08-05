package hu.me.iit.malus.thesis.emailservice.model;

import org.springframework.web.multipart.MultipartFile;
import java.util.Collection;

/**
 * The type Mail.
 */
public class Mail {

    private String from;
    private String[] to;
    private String subject;
    private String[] ccs;
    private String[] bccs;
    private String text;
    private String replyTo;
    private Collection<MultipartFile> attachments;

    /**
     * Instantiates a new Mail.
     */
    public Mail() {
    }


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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String[] getCcs() {
        return ccs;
    }

    public void setCcs(String[] ccs) {
        this.ccs = ccs;
    }

    public String[] getBccs() {
        return bccs;
    }

    public void setBccs(String[] bccs) {
        this.bccs = bccs;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public Collection<MultipartFile> getAttachments() {
        return attachments;
    }

    public void setAttachments(Collection<MultipartFile> attachments) {
        this.attachments = attachments;
    }
}
