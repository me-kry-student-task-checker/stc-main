package hu.me.iit.malus.thesis.course.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Getter @Setter
@ToString
public class CourseCreateDto {
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
}
