package hu.me.iit.malus.thesis.course.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

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
    private Date expiryDate;

    /**
     * Static factory method, which initializes expiry date of the token
     * @param studentId
     * @param courseId
     * @return new instance of Invitation with correctly set expiry date
     */
    public static Invitation of(String invitationUuid, String studentId, Long courseId) {
        Date expiryDate = calculateExpiryDate();
        return new Invitation(invitationUuid, studentId, courseId, expiryDate);
    }

    private static Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.HOUR, 24);
        return new Date(cal.getTime().getTime());
    }
}
