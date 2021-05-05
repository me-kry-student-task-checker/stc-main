package hu.me.iit.malus.thesis.task.repository;

import hu.me.iit.malus.thesis.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Set<Task>> findAllByCourseId(Long courseId);

    List<Task> deleteByCourseId(Long courseId);

}
