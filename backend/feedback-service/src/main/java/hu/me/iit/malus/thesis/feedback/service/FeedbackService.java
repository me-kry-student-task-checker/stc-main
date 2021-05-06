package hu.me.iit.malus.thesis.feedback.service;

import hu.me.iit.malus.thesis.feedback.model.CourseComment;
import hu.me.iit.malus.thesis.feedback.model.TaskComment;
import hu.me.iit.malus.thesis.feedback.service.exception.CommentNotFoundException;

import java.util.List;

/**
 * Interface of the Feedback Service
 * Defines all the possible operations for the service
 *
 * @author Attila Szőke
 */
public interface FeedbackService {

    /**
     * Creates a new course comment.
     *
     * @param courseComment the new course comment
     * @return the created course comment
     */
    CourseComment createCourseComment(CourseComment courseComment);

    /**
     * Creates a new task comment.
     *
     * @param taskComment the new task comment
     * @return the created task comment
     */
    TaskComment createTaskComment(TaskComment taskComment);

    /**
     * Returns all course comments filtered by their course ids
     *
     * @param courseId the filtered course id
     * @return list of course comments
     */
    List<CourseComment> getAllCourseComments(Long courseId);

    /**
     * Returns all task comments filtered by their task ids
     *
     * @param taskId the filtered task id
     * @return list of task comments
     */
    List<TaskComment> getAllTaskComments(Long taskId);

    /**
     * Removes a course comment.
     *
     * @param commentId the comment id
     * @throws CommentNotFoundException the comment not found exception
     */
    void removeCourseComment(Long commentId) throws CommentNotFoundException;

    /**
     * Removes a task comment.
     *
     * @param commentId the comment id
     * @throws CommentNotFoundException the comment not found exception
     */
    void removeTaskComment(Long commentId) throws CommentNotFoundException;

    /**
     * Deletes all feedbacks of a course.
     *
     * @param courseId the course dd
     */
    void removeFeedbacksByCourseId(Long courseId);

    /**
     * Deletes all feedbacks of a task.
     *
     * @param taskId the task id
     */
    void removeFeedbacksByTaskId(Long taskId);
}
