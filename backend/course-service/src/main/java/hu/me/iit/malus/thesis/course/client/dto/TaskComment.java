package hu.me.iit.malus.thesis.course.client.dto;

import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskComment {

    private Long id;
    private String authorId;
    private String text;
    private Date createDate;
    private Long taskId;
    private Set<File> files;
}
