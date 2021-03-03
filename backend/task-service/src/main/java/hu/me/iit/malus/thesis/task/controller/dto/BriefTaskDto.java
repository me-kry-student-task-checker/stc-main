package hu.me.iit.malus.thesis.task.controller.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class BriefTaskDto {

    private Long id;
    private String name;
    private String description;
    private Date creationDate;
    private boolean isDone;
    private Long courseId;
}
