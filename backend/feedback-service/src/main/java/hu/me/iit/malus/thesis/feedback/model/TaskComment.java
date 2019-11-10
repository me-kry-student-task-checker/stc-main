package hu.me.iit.malus.thesis.feedback.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("task")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class TaskComment extends Comment {
    private Long taskId;
}
