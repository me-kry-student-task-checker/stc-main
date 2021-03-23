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
    private Long tagId;

}
