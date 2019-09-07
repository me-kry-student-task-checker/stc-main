package hu.me.iit.malus.thesis.task.client;

import hu.me.iit.malus.thesis.task.client.dto.TaskComment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Feign client class for Feedback service
 *
 * @author Attila Szőke
 */
@FeignClient(name = "feedback-service")
public interface FeedbackClient {

    @GetMapping("/api/feedback/getAllTaskComments/{taskId}")
    List<TaskComment> getAllTaskComments(@PathVariable Long taskId);

}
