package hu.me.iit.malus.thesis.task.client;

import hu.me.iit.malus.thesis.dto.TaskComment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @PostMapping("/api/feedback/prepare/task/remove/by/{taskIds}")
    String prepareRemoveTaskCommentsByTaskIds(@PathVariable List<Long> taskIds);

    @PostMapping("/api/feedback/commit/task/remove/{taskCommentTransactionKey}")
    void commitRemoveTaskCommentsByTaskIds(@PathVariable String taskCommentTransactionKey);

    @PostMapping("/api/feedback/rollback/task/remove/{taskCommentTransactionKey}")
    void rollbackRemoveTaskCommentsByTaskIds(@PathVariable String taskCommentTransactionKey);
}
