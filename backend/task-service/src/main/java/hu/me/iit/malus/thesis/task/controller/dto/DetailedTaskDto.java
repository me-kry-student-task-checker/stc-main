package hu.me.iit.malus.thesis.task.controller.dto;

import hu.me.iit.malus.thesis.task.client.dto.File;
import hu.me.iit.malus.thesis.task.client.dto.Student;
import hu.me.iit.malus.thesis.task.client.dto.TaskComment;
import hu.me.iit.malus.thesis.task.model.Task;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * A more detailed Task DTO, used as query response
 * @author Javorek Dénes
 */
@Getter @Setter
@NoArgsConstructor
@ToString
public class DetailedTaskDto {
    private Long id;
    private String name;
    private String description;
    private Date creationDate;
    private Set<File> files;
    private boolean isDone;
    private Long courseId;
    private Set<Student> helpNeededStudents;
    private Set<Student> completedStudents;
    private List<TaskComment> comments;

    public DetailedTaskDto(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        this.creationDate = task.getCreationDate();
        this.isDone = task.isDone();
        this.courseId = task.getCourseId();
    }
}
