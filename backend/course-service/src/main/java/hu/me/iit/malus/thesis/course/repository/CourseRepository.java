package hu.me.iit.malus.thesis.course.repository;

import hu.me.iit.malus.thesis.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
