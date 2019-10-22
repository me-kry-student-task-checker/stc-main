package hu.me.iit.malus.thesis.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * Representation of Teacher type User
 * @author Javorek Dénes
 */
@Entity
@Getter @Setter
@ToString
@NoArgsConstructor
public class Teacher extends User {

    @ElementCollection
    @OrderColumn(name = "index_no")
    @CollectionTable(name = "teacher_created",
            joinColumns = @JoinColumn(name = "user_email"))
    @Column(name="createdCourses")
    private List<Long> createdCourseIds;

    public Teacher(String email, String password, String firstName, String lastName, List<Long> createdCourseIds) {
        super(email, password, firstName, lastName, UserRole.TEACHER, false);
        this.createdCourseIds = createdCourseIds;
    }
}
