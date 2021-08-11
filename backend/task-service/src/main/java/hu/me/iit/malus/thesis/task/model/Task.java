package hu.me.iit.malus.thesis.task.model;

import hu.me.iit.malus.thesis.dto.File;
import hu.me.iit.malus.thesis.dto.TaskComment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Data model for task object, foundation of this service.
 *
 * @author Attila Szőke
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Task {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="native")
    @GenericGenerator(name="native", strategy="native")
    private Long id;
    private String name;
    private String description;
    private Date creationDate;
    private boolean isDone;
    private Long courseId;
    @Transient
    private Set<File> files;
    @ElementCollection
    @OrderColumn(name = "help_index_no")
    @CollectionTable(name = "help_needed",
            joinColumns = @JoinColumn(name = "task_id"))
    private Set<String> helpNeededStudentIds;
    @ElementCollection
    @OrderColumn(name = "completed_index_no")
    @CollectionTable(name = "completed",
            joinColumns = @JoinColumn(name = "task_id"))
    private Set<String> completedStudentIds;
    @Transient
    private List<TaskComment> comments;

    public Task(
            String name, String description, Date creationDate, boolean isDone, Long courseId,
            Set<String> helpNeededStudentIds, Set<String> completedStudentIds
    ) {
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.isDone = isDone;
        this.courseId = courseId;
        this.helpNeededStudentIds = helpNeededStudentIds;
        this.completedStudentIds = completedStudentIds;
    }

    /**
     * Adds a new student to the need help set
     *
     * @param studentIdToAdd this student's id will be added
     */
    public void addStudentIdToHelp(String studentIdToAdd) {
        helpNeededStudentIds.add(studentIdToAdd);
    }

    /**
     * Adds a new student to the completed set
     *
     * @param studentIdToAdd this student's id will be added
     */
    public void addStudentIdToCompleted(String studentIdToAdd) {
        completedStudentIds.add(studentIdToAdd);
    }
}
