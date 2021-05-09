package hu.me.iit.malus.thesis.course.controller.dto;

import hu.me.iit.malus.thesis.dto.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CourseFullDetailsDto {

    private Long id;
    private String name;
    private String description;
    private Date creationDate;
    private Teacher creator;
    private Set<Student> students = new HashSet<>();
    private Set<Task> tasks = new HashSet<>();
    private List<CourseComment> comments = new ArrayList<>();
    private Set<File> files = new HashSet<>();
}
