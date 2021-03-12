package hu.me.iit.malus.thesis.task.controller.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class BriefTaskDto {

    @Min(1)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Date creationDate;

    private boolean isDone;

    @Min(1)
    private Long courseId;
}
