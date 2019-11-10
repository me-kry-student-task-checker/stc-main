package hu.me.iit.malus.thesis.course.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter @Setter
@ToString
public class CourseModificationDto {
    private Long id;
    private String name;
    private String description;
}
