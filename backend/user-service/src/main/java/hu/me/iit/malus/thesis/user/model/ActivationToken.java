package hu.me.iit.malus.thesis.user.model;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter @Setter
public class ActivationToken {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="native")
    @GenericGenerator(name="native", strategy="native")
    private Long id;
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    private Date expiryDate;

    private ActivationToken(String token, User user, Date expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }

    /**
     * Static factory method, which initializes expiry date of the token
     * @param token
     * @param user
     * @return new instance of ActivationToken with correctly set fields
     */
    public static ActivationToken of(String token, User user) {
        Date expiryDate = calculateExpiryDate();
        return new ActivationToken(token, user, expiryDate);
    }

    private static Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, EXPIRATION);
        return new Date(cal.getTime().getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivationToken that = (ActivationToken) o;
        return Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
