package hu.me.iit.malus.thesis.feedback.controller.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class TaskCommentCreateDto {

    @NotBlank
    private String text;

    @Min(1)
    private Long taskId;
}
