package hu.me.iit.malus.thesis.feedback.controller.dto;

import java.util.Date;
import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import hu.me.iit.malus.thesis.dto.File;
import lombok.Data;

@Data
public class CommentDetailsDto {
    @Min(1)
    private Long id;

    @NotBlank
    private String authorId;

    @NotBlank
    private String text;

    @NotNull
    private Date createDate;

    @NotNull
    private Set<File> files;
}
