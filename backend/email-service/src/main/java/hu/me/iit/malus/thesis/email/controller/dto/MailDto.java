package hu.me.iit.malus.thesis.email.controller.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Data transfer object for sending mails.
 *
 * @author Attila Szőke
 */
@Data
public class MailDto {

    @NotEmpty(message = "To cannot be empty")
    private List<@Email String> to;

    @NotBlank(message = "Subject cannot be empty")
    private String subject;

    private String[] ccs;

    private String[] bccs;

    @NotBlank(message = "Email content cannot be blank")
    private String text;

    private String replyTo;
}
