package hu.me.iit.malus.thesis.course.client;

import hu.me.iit.malus.thesis.course.client.dto.CourseComment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Mocked Feign client class for Feedback service
 * @author Attila Szőke
 */
public class FeedbackClient {

    //FIXME when feedback service is ready, replace this with a Feign interface
    private static List<CourseComment> comments = new ArrayList<>();

    {
        comments.add(new CourseComment(1L, new Date(), "lala@lali.com", "minden jó"));
        comments.add(new CourseComment(3L, new Date(), "a@b.com", "minden jó, nem"));
    }

    public static void save(List<CourseComment> courseComment) {
        comments.addAll(courseComment);
    }

    public static List<CourseComment> getAll() {
        return comments;
    }

    public static List<CourseComment> getByCourseId(Long courseId) {
        List<CourseComment> filteredComments = new ArrayList<>();
        for (CourseComment comment: comments) {
            if (comment.getCourseId().equals(courseId)) {
                filteredComments.add(comment);
            }
        }
        return filteredComments;
    }

}
