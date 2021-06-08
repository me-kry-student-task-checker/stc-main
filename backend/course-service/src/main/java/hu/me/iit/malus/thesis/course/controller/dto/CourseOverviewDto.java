package hu.me.iit.malus.thesis.course.controller.dto;

import hu.me.iit.malus.thesis.dto.Teacher;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class CourseOverviewDto {
    @Min(1)
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    private Date creationDate;
    @NotEmpty
    private Teacher creator;
}
