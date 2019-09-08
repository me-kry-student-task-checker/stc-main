package hu.me.iit.malus.thesis.feedback.model;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("task")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TaskComment extends Comment {

    private Long taskId;
}
