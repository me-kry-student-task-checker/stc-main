package hu.me.iit.malus.thesis.course.controller.dto;

import hu.me.iit.malus.thesis.course.client.dto.Teacher;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * Course DTO object, that is used when only the most important information
 * is needed about the courses
 * @author Javorek Dénes
 */
@NoArgsConstructor
@Getter @Setter
@ToString
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
