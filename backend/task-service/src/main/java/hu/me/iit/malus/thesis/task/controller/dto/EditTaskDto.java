package hu.me.iit.malus.thesis.task.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * A simple Task DTO, used as create and edit request
 *
 * @author Krisztián Ilku
 */
@Data
@NoArgsConstructor
public class EditTaskDto {

    @Min(1)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @Min(1)
    private Long courseId;
}
