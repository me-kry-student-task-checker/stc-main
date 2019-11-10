package hu.me.iit.malus.thesis.user.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * Representation of Student type User
 * @author Javorek Dénes
 */
@Entity
@Getter @Setter
@ToString
@NoArgsConstructor
public class Student extends User {

    @ElementCollection
    @OrderColumn(name = "index_no")
    @CollectionTable(name = "student_assigned",
            joinColumns = @JoinColumn(name = "user_email"))
    @Column(name="assignedCourses")
    private List<Long> assignedCourseIds;

    public Student(String email, String password, String firstName, String lastName, List<Long> assignedCourseIds) {
        super(email, password, firstName, lastName, UserRole.STUDENT, false);
        this.assignedCourseIds = assignedCourseIds;
    }
}
