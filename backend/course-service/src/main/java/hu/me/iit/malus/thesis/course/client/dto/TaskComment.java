package hu.me.iit.malus.thesis.course.client.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TaskComment {

    private Long id;
    private Long courseId;
    private Date creationDate;
    private String authorId;
    private String body;

}
