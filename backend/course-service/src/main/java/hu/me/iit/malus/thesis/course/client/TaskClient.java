package hu.me.iit.malus.thesis.course.client;

import hu.me.iit.malus.thesis.course.client.dto.Task;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

}
