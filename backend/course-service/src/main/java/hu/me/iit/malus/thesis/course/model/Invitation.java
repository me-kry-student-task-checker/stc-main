package hu.me.iit.malus.thesis.course.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Data model for invitation object
 *
 * @author Attila Szőke
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Invitation {

    @Id
    private String invitationUuid;
    private String studentId;
    private Long courseId;
}
