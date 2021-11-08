package hu.me.iit.malus.thesis.task.repository;

import hu.me.iit.malus.thesis.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByIdAndRemovedFalse(Long id);

    List<Task> findAllByCourseIdAndRemovedFalse(Long courseId);
}
