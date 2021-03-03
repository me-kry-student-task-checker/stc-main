package hu.me.iit.malus.thesis.task.controller.dto;

import lombok.*;

/**
 * A simple Task DTO, used as create and edit request
 *
 * @author Krisztián Ilku
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class EditTaskDto {

    private Long id;
    private String name;
    private String description;
    private Long courseId;
}
