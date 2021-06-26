package hu.me.iit.malus.thesis.feedback.model;

import hu.me.iit.malus.thesis.dto.File;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

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
@DiscriminatorColumn(name = "comment_type", discriminatorType = DiscriminatorType.STRING)
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String authorId;
    private String text;
    private Date createDate;

    @Transient
    private Set<File> files;
}
