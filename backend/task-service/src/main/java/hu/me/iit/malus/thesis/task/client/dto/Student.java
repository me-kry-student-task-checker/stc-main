package hu.me.iit.malus.thesis.task.client.dto;

import lombok.*;

import java.util.List;

/**
 * Data Transfer Object for Student entity
 *
 * @author Javorek Dénes
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Student extends User {

    private List<Long> assignedCourseIds;

    public Student(String id, String firstName, String lastName, List<Long> assignedCourseIds) {
        super(id, firstName, lastName);
        this.assignedCourseIds = assignedCourseIds;
    }
}
