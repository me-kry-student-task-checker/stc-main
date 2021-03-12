package hu.me.iit.malus.thesis.course.controller.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Getter @Setter
@ToString
@Builder
@AllArgsConstructor
public class CourseModificationDto {
    @Min(1)
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
}
