package hu.me.iit.malus.thesis.feedback.model;

import com.google.common.base.Objects;
import hu.me.iit.malus.thesis.feedback.client.dto.File;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@DiscriminatorColumn(name="comment_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Comment {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="native")
    @GenericGenerator(name="native", strategy="native")
    private Long id;
    private String authorId;
    private String text;
    private Date createDate;
    @Transient private Set<File> files;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return Objects.equal(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
