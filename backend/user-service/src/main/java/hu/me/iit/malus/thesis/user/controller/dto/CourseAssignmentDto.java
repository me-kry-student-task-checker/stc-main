package hu.me.iit.malus.thesis.user.controller.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@RequiredArgsConstructor
public class CourseAssignmentDto {

    @Min(1)
    private final long courseId;
    @Size(min = 1)
    private final List<String> studentEmails;
}
