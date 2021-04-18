package hu.me.iit.malus.thesis.feedback.client.dto;

import lombok.Data;

import java.util.Date;


@Data
public class ActivityDto {

    private Long id;
    private ActivityType type;
    private Date createDate;
    private Long parentId;

    public enum ActivityType {
        FEEDBACK_COURSE,
        FEEDBACK_TASK
    }
}
