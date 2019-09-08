package hu.me.iit.malus.thesis.feedback.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * Data model for comment object, foundation of this service.
 * It is extended by TaskComment and CourseComment.
 *
 * @author Szőke Attila
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="comment_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Comment {

    @Id
    @GeneratedValue
    private Long id;
    private String authorId;
    private String text;
    private Date createDate;

}
