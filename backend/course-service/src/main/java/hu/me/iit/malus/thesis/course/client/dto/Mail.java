package hu.me.iit.malus.thesis.course.client.dto;

import lombok.*;

import javax.validation.constraints.Email;
import java.util.List;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor
public class Mail {

    private List<@Email String> to;
    private String subject;
    private String[] ccs;
    private String[] bccs;
    private String text;
    private String replyTo;
}
