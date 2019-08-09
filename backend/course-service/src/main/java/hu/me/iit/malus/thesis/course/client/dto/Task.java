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

    public Task(String name) {
        this.name = name;
    }
}
