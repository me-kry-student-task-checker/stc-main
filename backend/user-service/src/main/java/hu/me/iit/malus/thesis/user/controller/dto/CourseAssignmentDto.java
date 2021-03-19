package hu.me.iit.malus.thesis.user.controller.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CourseAssignmentDto {

    private final long courseId;
    private final List<String> studentEmails;
}
