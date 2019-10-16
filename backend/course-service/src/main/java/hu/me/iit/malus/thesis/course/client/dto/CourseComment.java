package hu.me.iit.malus.thesis.course.client.dto;

import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CourseComment {

    private Long id;
    private String authorId;
    private String text;
    private Date createDate;
    private Long courseId;
    private Set<File> files;
}
