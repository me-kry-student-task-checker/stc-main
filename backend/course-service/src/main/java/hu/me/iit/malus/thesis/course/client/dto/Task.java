package hu.me.iit.malus.thesis.course.client.dto;

import lombok.*;

/**
 * Data Transfer Object for Task entity
 * @author Javorek Dénes
 */
@Getter @Setter @NoArgsConstructor
@ToString @EqualsAndHashCode
public class Task {

    private Long id;
    private String name;
    private Long courseId;

    public Task(String name, Long courseId) {
        this.name = name;
        this.courseId = courseId;
    }
}
