package hu.me.iit.malus.thesis.task.repository;

import hu.me.iit.malus.thesis.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
