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

    private List<CourseComment> comments = new ArrayList<>();

    {
        comments.add(new CourseComment(1L, new Date(), "lala@lali.com", "minden jó"));
    }

    public void save(CourseComment courseComment){
        comments.add(courseComment);
    }

    public List<CourseComment> getAll(){
        return comments;
    }

    public CourseComment getByCourseId(Long courseId){
        for (CourseComment comment: comments) {
            if (comment.getCourseId() == courseId){
                return comment;
            }
        }
        return new CourseComment();
    }

}
