package hu.me.iit.malus.thesis.feedback.model;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("task")
@Data
public class TaskComment extends Comment {
    private Long taskId;
}
