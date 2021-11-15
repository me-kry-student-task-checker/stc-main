package hu.me.iit.malus.thesis.course.client;

import hu.me.iit.malus.thesis.dto.Task;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Set;

/**
 * Feign client class for the Task service
 *
 * @author Attila Szőke
 */
@FeignClient(name = "task-service")
public interface TaskClient {

    @GetMapping("/api/task/getAll/{courseId}")
    Set<Task> getAllTasks(@PathVariable Long courseId);

    @PostMapping("/api/task/prepare/remove/by/taskIds/{taskIds}")
    String prepareRemoveTaskByTaskIds(@PathVariable List<Long> taskIds);

    @PostMapping("/api/task/commit/remove/{taskTransactionKey}")
    void commitRemoveTaskByCourseId(@PathVariable String taskTransactionKey);

    @PostMapping("/api/task/rollback/remove/{taskTransactionKey}")
    void rollbackRemoveTaskByCourseId(@PathVariable String taskTransactionKey);
}
