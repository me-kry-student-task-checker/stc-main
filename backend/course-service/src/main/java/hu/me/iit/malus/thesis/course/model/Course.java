package hu.me.iit.malus.thesis.course.model;

import com.google.common.base.Objects;
import hu.me.iit.malus.thesis.dto.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

/**
 * Data model for course object, foundation of this service.
 * @author Javorek Dénes
 */
@Entity
@Getter @Setter @NoArgsConstructor
@ToString
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String name;
    private String description;
    private Date creationDate;
    private boolean removed;

    @Transient
    private Teacher creator;
    @Transient
    private Set<Student> students = new HashSet<>();
    @Transient
    private Set<Task> tasks = new HashSet<>();
    @Transient
    private List<CourseComment> comments = new ArrayList<>();
    @Transient
    private Set<File> files = new HashSet<>();

    public Course(String name, String description, Teacher creator) {
        this.name = name;
        this.description = description;
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equal(id, course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
