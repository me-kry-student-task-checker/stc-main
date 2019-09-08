package hu.me.iit.malus.thesis.course.client.dto;

import lombok.*;

import java.util.Date;

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
}
