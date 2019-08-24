package hu.me.iit.malus.thesis.task.client;

import hu.me.iit.malus.thesis.task.client.dto.TaskComment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Mocked Feign client class for Feedback service
 *
 * @author Attila Szőke
 */
public class FeedbackClient {

    //FIXME when feedback service is ready, replace this with a Feign interface
    private static List<TaskComment> comments = new ArrayList<>();

    {
        comments.add(new TaskComment(1L, new Date(), "lala@lali.com", "minden jó"));
        comments.add(new TaskComment(3L, new Date(), "a@b.com", "minden jó, nem"));
    }

    public static void save(List<TaskComment> taskComment) {
        comments.addAll(taskComment);
    }

    public static List<TaskComment> getAll() {
        return comments;
    }

    public static List<TaskComment> getByTaskId(Long taskId) {
        List<TaskComment> filteredComments = new ArrayList<>();
        for (TaskComment comment : comments) {
            if (comment.getCourseId().equals(taskId)) {
                filteredComments.add(comment);
            }
        }
        return filteredComments;
    }

}
