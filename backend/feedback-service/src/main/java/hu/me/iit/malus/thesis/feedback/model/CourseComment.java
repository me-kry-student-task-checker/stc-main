package hu.me.iit.malus.thesis.feedback.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("course")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CourseComment extends Comment {
    private Long courseId;
}
