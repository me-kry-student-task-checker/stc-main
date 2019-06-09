package hu.me.iit.malus.thesis.emailservice.model;

/**
 * The type Mail.
 */
public class Mail {

    private String to;
    private String subject;
    private String[] bccs;
    private String text;

    /**
     * Instantiates a new Mail.
     */
    public Mail() {
    }

    /**
     * Instantiates a new Mail.
     *
     * @param to      the to
     * @param subject the subject
     * @param bccs    the bccs
     * @param text    the text
     */
    public Mail(String to, String subject, String[] bccs, String text) {
        this.to = to;
        this.subject = subject;
        this.bccs = bccs;
        this.text = text;
    }

    /**
     * Gets to.
     *
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets to.
     *
     * @param to the to
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Gets subject.
     *
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets subject.
     *
     * @param subject the subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Get bccs string [ ].
     *
     * @return the string [ ]
     */
    public String[] getBccs() {
        return bccs;
    }

    /**
     * Sets bccs.
     *
     * @param bccs the bccs
     */
    public void setBccs(String[] bccs) {
        this.bccs = bccs;
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets text.
     *
     * @param text the text
     */
    public void setText(String text) {
        this.text = text;
    }
}
