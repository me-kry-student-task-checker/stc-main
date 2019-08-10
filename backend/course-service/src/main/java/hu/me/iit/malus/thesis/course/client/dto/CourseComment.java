package hu.me.iit.malus.thesis.course.client.dto;

import lombok.*;

import java.util.Date;

/**
 * Data Transfer Object for Course Comment entity
 * @author Javorek Dénes
 */
@Getter @Setter @NoArgsConstructor
@ToString @EqualsAndHashCode
public class CourseComment {
    private Long id;
    private Long courseId;
    private Date creationDate;
    private User author;
    private String body;

    //TODO: Handle Uploaded Subject Material

    public CourseComment(Long courseId, Date creationDate, User author, String body) {
        this.courseId = courseId;
        this.creationDate = creationDate;
        this.author = author;
        this.body = body;
    }
}
