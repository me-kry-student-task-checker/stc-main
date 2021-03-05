package hu.me.iit.malus.thesis.task.controller.dto;

import hu.me.iit.malus.thesis.task.client.dto.File;
import hu.me.iit.malus.thesis.task.client.dto.Student;
import hu.me.iit.malus.thesis.task.client.dto.TaskComment;
import hu.me.iit.malus.thesis.task.model.Task;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * A more detailed Task DTO, used as query response
 *
 * @author Javorek Dénes
 */
@Data
public class DetailedTaskDto {

    @Min(1)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Date creationDate;

    @NotNull
    private Set<File> files;

    private boolean isDone;

    @Min(1)
    private Long courseId;

    @NotNull
    private Set<Student> helpNeededStudents;

    @NotNull
    private Set<Student> completedStudents;

    @NotNull
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
