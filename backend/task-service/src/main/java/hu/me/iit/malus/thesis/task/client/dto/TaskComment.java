package hu.me.iit.malus.thesis.task.client.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TaskComment {

    private Long id;
    private String authorId;
    private String text;
    private Date createDate;
    private Long taskId;
}
