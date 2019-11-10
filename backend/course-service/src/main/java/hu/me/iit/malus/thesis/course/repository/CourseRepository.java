package hu.me.iit.malus.thesis.course.repository;

import hu.me.iit.malus.thesis.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Set<Course> findAllByIdIsIn(Set<Long> ids);
}
