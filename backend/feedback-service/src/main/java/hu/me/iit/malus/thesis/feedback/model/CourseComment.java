package hu.me.iit.malus.thesis.feedback.model;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("course")
@Data
public class CourseComment extends Comment {
    private Long courseId;
}
