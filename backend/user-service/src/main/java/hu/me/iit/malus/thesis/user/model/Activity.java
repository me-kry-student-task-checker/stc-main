package hu.me.iit.malus.thesis.user.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Activity {

    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private ActivityType type;
    @CreationTimestamp
    private Date createDate;
    private Long parentId;

    public enum ActivityType {
        FEEDBACK_COURSE,
        FEEDBACK_TASK
    }
}
