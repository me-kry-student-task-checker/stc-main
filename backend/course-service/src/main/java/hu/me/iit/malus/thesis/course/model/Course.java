package hu.me.iit.malus.thesis.course.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

/**
 * Data model for course object, foundation of this service
 * @author Javorek Dénes
 */
@Entity
@Getter @Setter @NoArgsConstructor
@ToString @EqualsAndHashCode
public class Course {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;

    //TODO: Change types to valid Teacher and Student objects when those are available
    @Transient private Object creator;
    @Transient private Set<Object> students = new HashSet<>();

    //TODO: Change type to Task object when available
    @Transient private Set<Object> tasks = new HashSet<>();

    public Course(String name, String description, Object creator) {
        this.name = name;
        this.description = description;
        this.creator = creator;
    }
}
