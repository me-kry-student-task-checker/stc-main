package hu.me.iit.malus.thesis.email.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import java.util.List;

/**
 * The main model object of the service.
 *
 * @author Ilku Krisztián
 * @author Attila Szőke
 */
@Data
@AllArgsConstructor
public class Mail { // Not used for now, if emails will be stored it can be used, if not should be deleted
    private List<@Email String> to;
    private String subject;
    private String[] ccs;
    private String[] bccs;
    private String text;
    private String replyTo;
}
