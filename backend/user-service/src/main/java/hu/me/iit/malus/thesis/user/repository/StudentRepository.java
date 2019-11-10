package hu.me.iit.malus.thesis.user.repository;

import hu.me.iit.malus.thesis.user.model.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data repository, that handles all db operations for Student objects
 * @author Javorek Dénes
 */
public interface StudentRepository extends UserBaseRepository<Student> {
    @Query("SELECT s FROM Student s WHERE :courseId MEMBER OF s.assignedCourseIds")
    List<Student> findAllAssignedForCourseId(@Param("courseId") Long courseId);

    @Query("SELECT s FROM Student s WHERE :courseId NOT MEMBER OF s.assignedCourseIds")
    List<Student> findAllNotAssignedForCourseId(@Param("courseId") Long courseId);
}
