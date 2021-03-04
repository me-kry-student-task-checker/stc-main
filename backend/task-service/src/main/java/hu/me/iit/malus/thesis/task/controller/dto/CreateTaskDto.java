package hu.me.iit.malus.thesis.task.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class CreateTaskDto {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @Min(1)
    private Long courseId;
}
