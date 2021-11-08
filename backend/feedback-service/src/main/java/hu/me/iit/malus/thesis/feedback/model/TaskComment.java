package hu.me.iit.malus.thesis.feedback.model;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
@DiscriminatorValue("task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskComment extends Comment {
    private Long taskId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskComment)) return false;
        TaskComment that = (TaskComment) o;
        return Objects.equals(taskId, that.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }
}
