package hu.me.iit.malus.thesis.user.controller.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
public class StudentDto extends UserDto {

    @NotNull
    private List<Long> assignedCourseIds;
}
