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

    @Transient
    private List<TaskComment> comments;
    @Transient
    private Set<Long> completedStudentIds;
    @Transient
    private Set<Long> helpNeededStudentIds;

}
