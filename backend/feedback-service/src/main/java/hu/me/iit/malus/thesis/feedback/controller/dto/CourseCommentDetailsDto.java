package hu.me.iit.malus.thesis.feedback.controller.dto;

import hu.me.iit.malus.thesis.dto.File;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
public class CourseCommentDetailsDto {

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

    @Min(1)
    private Long courseId;

}
