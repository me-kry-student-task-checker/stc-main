package hu.me.iit.malus.thesis.task.controller.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CreateTaskDto {

    private String name;
    private String description;
    private Long courseId;
}
