package hu.me.iit.malus.thesis.task.client.dto;

import lombok.*;

import java.util.Date;

/**
 * Data Transfer Object for Course Comment entity
 *
 * @author Attila Szőke
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TaskComment {
    private Long id;
    private Long courseId;
    private Date creationDate;
    private String authorId;
    private String body;

    //TODO: Handle Uploaded Subject Material

    public TaskComment(Long courseId, Date creationDate, String authorId, String body) {
        this.courseId = courseId;
        this.creationDate = creationDate;
        this.authorId = authorId;
        this.body = body;
    }
}
