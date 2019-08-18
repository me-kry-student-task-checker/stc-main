package hu.me.iit.malus.thesis.course.repository;

import hu.me.iit.malus.thesis.course.model.Course;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Javorek Dénes
 */
public interface CourseRepository extends CrudRepository<Course, Long> {
}
