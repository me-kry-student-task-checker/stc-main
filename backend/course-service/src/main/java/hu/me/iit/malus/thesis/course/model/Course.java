package hu.me.iit.malus.thesis.course.model;

import hu.me.iit.malus.thesis.course.client.dto.CourseComment;
import hu.me.iit.malus.thesis.course.client.dto.Student;
import hu.me.iit.malus.thesis.course.client.dto.Task;
import hu.me.iit.malus.thesis.course.client.dto.Teacher;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.*;

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
    @Transient private Set<String> tags = new HashSet<>();

    //TODO: If fileupload is ready, create a new CourseDescription class next to course and use that here
    private String description;

    private Date creationDate;
    @Transient private Teacher creator;
    @Transient private Set<Student> students = new HashSet<>();
    @Transient private Set<Task> tasks = new HashSet<>();
    @Transient private List<CourseComment> comments = new ArrayList<>();

    public Course(String name, Set<String> tags, String description, Teacher creator) {
        this.name = name;
        this.tags = tags;
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
     * Adds more students for this course.
     * @param studentsToAdd This student will be added
     */
    public void addAllStudent(Set<Student> studentsToAdd) {
        students.addAll(studentsToAdd);
    }

    /**
     * Adds a task under this course.
     * @param taskToAdd
     */
    public void addTask(Task taskToAdd) {
        tasks.add(taskToAdd);
    }

    /**
     * Adds more tasks under this course.
     * @param tasksToAdd
     */
    public void addAllTasks(Set<Task> tasksToAdd) {
        tasks.addAll(tasksToAdd);
    }

    /**
     * Adds a comment under this course.
     * @param commentToAdd
     */
    public void addComment(CourseComment commentToAdd) {
        comments.add(commentToAdd);
    }

    /**
     * Adds more comments under this course.
     * @param commentsToAdd
     */
    public void addAllComment(Set<CourseComment> commentsToAdd) {
        comments.addAll(commentsToAdd);
    }
}
