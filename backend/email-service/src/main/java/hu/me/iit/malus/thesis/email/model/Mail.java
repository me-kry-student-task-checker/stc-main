package hu.me.iit.malus.thesis.email.model;

import org.springframework.web.multipart.MultipartFile;
import java.util.Collection;
import java.util.List;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Ilku Krisztián
 * The type Mail.
 */

@Getter @Setter @ToString
@EqualsAndHashCode @NoArgsConstructor
public class Mail {

    @NotNull(message = "Sender cannot be null")
    @NotEmpty(message = "Sender cannot be empty")
    @Email(message = "Please provide a valid email")
    private String from;

    @NotNull(message = "To cannot be null")
    @NotEmpty(message = "To cannot be empty")
    private List<@Email String> to;

    @NotNull(message = "Subject cannot be null")
    @NotEmpty(message = "Subject cannot be empty")
    private String subject;

    private String[] ccs;

    private String[] bccs;

    @NotNull(message = "Email content cannot be null")
    @NotEmpty(message = "Email content cannot be empty")
    private String text;

    private String replyTo;

    private Collection<MultipartFile> attachments;
}
