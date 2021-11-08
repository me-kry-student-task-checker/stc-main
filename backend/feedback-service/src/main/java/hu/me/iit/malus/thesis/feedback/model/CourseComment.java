package hu.me.iit.malus.thesis.feedback.model;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
@DiscriminatorValue("course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CourseComment extends Comment {
    private Long courseId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseComment)) return false;
        CourseComment that = (CourseComment) o;
        return Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }
}
