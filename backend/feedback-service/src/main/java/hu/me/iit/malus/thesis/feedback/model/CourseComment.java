package hu.me.iit.malus.thesis.feedback.model;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("course")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CourseComment extends Comment {

    private Long courseId;
}
