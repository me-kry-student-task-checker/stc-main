package hu.me.iit.malus.thesis.course.model;

import hu.me.iit.malus.thesis.course.client.dto.Student;
import hu.me.iit.malus.thesis.course.client.dto.Task;
import hu.me.iit.malus.thesis.course.client.dto.Teacher;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

/**
 * Data model for course object, foundation of this service.
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

    @Transient private Teacher creator;
    @Transient private Set<Student> students = new HashSet<>();
    @Transient private Set<Task> tasks = new HashSet<>();

    public Course(String name, String description, Teacher creator) {
        this.name = name;
        this.description = description;
        this.creator = creator;
    }

    /**
     * Adds a new student for this course.
     * @param studentToAdd This student will be added
     */
    public void addStudent(Student studentToAdd) {
        students.add(studentToAdd);
    }

    /**
     * Adds a task under this course.
     * @param taskToAdd
     */
    public void addTask(Task taskToAdd) {
        tasks.add(taskToAdd);
    }
}
