package hu.me.iit.malus.thesis.task.model;

import hu.me.iit.malus.thesis.task.client.dto.TaskComment;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Data model for task object, foundation of this service.
 *
 * @author Attila Szőke
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Task {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private Date creationDate;
    private boolean isDone;
    private Long courseId;
    private Set<Long> helpNeededStudentIds;
    private Set<Long> completedStudentIds;

    @Transient
    private List<TaskComment> comments;

}
