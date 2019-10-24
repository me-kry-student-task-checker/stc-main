package hu.me.iit.malus.thesis.user.repository;

import hu.me.iit.malus.thesis.user.model.Teacher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Spring Data repository, that handles all db operations for Teacher objects
 * @author Javorek Dénes
 */
public interface TeacherRepository extends UserBaseRepository<Teacher> {
    @Query("SELECT t FROM Teacher t WHERE :courseId MEMBER OF t.createdCourseIds")
    Optional<Teacher> findByCreatedCourseId(@Param("courseId") Long courseId);
}
