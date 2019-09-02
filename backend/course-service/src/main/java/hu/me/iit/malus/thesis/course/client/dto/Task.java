package hu.me.iit.malus.thesis.course.client.dto;

import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Data Transfer Object for Task entity
 *
 * @author Attila Szőke
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString @EqualsAndHashCode
public class Task {

    private Long id;
    private String name;
    private String description;
    private Date creationDate;
    private boolean isDone;
    private Long courseId;
    private Set<String> helpNeededStudentIds;
    private Set<String> completedStudentIds;
    private List<TaskComment> comments;

}
