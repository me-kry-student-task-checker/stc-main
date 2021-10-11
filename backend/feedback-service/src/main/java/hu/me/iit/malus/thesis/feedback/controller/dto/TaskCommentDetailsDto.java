package hu.me.iit.malus.thesis.feedback.controller.dto;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class TaskCommentDetailsDto extends CommentDetailsDto{

    @Min(1)
    private Long taskId;

}
