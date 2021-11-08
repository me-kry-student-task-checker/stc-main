package hu.me.iit.malus.thesis.course.repository;

import hu.me.iit.malus.thesis.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByIdAndRemovedFalse(Long id);

    Set<Course> findAllByIdIsInAndRemovedFalse(Set<Long> ids);
}
