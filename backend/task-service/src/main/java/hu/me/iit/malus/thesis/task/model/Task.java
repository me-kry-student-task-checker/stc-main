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
    private boolean removed;

    @Transient
    private Set<File> files;
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

    public void toggleCompletedStudent(String studentEmail) {
        if (completedStudentIds.contains(studentEmail)) {
            completedStudentIds.remove(studentEmail);
            return;
        }
        completedStudentIds.add(studentEmail);
    }

    public void toggleHelpNeededStudent(String studentEmail) {
        if (helpNeededStudentIds.contains(studentEmail)) {
            helpNeededStudentIds.remove(studentEmail);
            return;
        }
        helpNeededStudentIds.add(studentEmail);
    }
}
