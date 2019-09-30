package hu.me.iit.malus.thesis.feedback.model;

import hu.me.iit.malus.thesis.feedback.client.dto.File;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

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
    @Transient private Set<File> files;
}
