package hu.me.iit.malus.thesis.user.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Activity {

    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private ActivityType type;

    public enum ActivityType {
        FEEDBACK_COURSE,
        FEEDBACK_TASK
    }
}
