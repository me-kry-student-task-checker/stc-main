package hu.me.iit.malus.thesis.feedback.service;

import hu.me.iit.malus.thesis.feedback.controller.dto.CourseCommentCreateDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.CourseCommentDetailsDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.TaskCommentCreateDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.TaskCommentDetailsDto;
import hu.me.iit.malus.thesis.feedback.service.exception.CommentNotFoundException;
import hu.me.iit.malus.thesis.feedback.service.exception.ForbiddenCommentEditException;

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
     * @param dto      the dto
     * @param authorId the author id
     * @return the created course comment
     */
    CourseCommentDetailsDto createCourseComment(CourseCommentCreateDto dto, String authorId);

    /**
     * Creates a new task comment.
     *
     * @param dto      the dto
     * @param authorId the author id
     * @return the created task comment
     */
    TaskCommentDetailsDto createTaskComment(TaskCommentCreateDto dto, String authorId);

    /**
     * Returns all course comments filtered by their course ids
     *
     * @param courseId the filtered course id
     * @return list of course comments
     */
    List<CourseCommentDetailsDto> getAllCourseComments(Long courseId);

    /**
     * Returns all task comments filtered by their task ids
     *
     * @param taskId the filtered task id
     * @return list of task comments
     */
    List<TaskCommentDetailsDto> getAllTaskComments(Long taskId);

    /**
     * Removes a course comment.
     *
     * @param commentId the comment id
     * @param authorId  the author id
     * @throws CommentNotFoundException      the comment not found exception
     * @throws ForbiddenCommentEditException the forbidden comment edit exception
     */
    void removeCourseComment(Long commentId, String authorId) throws CommentNotFoundException, ForbiddenCommentEditException;

    /**
     * Removes a task comment.
     *
     * @param commentId the comment id
     * @param authorId  the author id
     * @throws CommentNotFoundException      the comment not found exception
     * @throws ForbiddenCommentEditException the forbidden comment edit exception
     */
    void removeTaskComment(Long commentId, String authorId) throws CommentNotFoundException, ForbiddenCommentEditException;

    /**
     * 2PC prepare phase, prepares removal of course comments by the course id.
     *
     * @param courseId the course id
     * @return the transaction key
     */
    String prepareRemoveCourseCommentsByCourseId(Long courseId);

    /**
     * 2PC commit phase, commits removal of course comments by transaction key returned by prepare method.
     *
     * @param transactionKey the transaction key
     */
    void commitRemoveCourseCommentsByCourseId(String transactionKey);

    /**
     * 2PC rollback phase, rolls back removal of course comments by transaction key returned by prepare method.
     *
     * @param transactionKey the transaction key
     */
    void rollbackRemoveCourseCommentsByCourseId(String transactionKey);

    /**
     * 2PC prepare phase, prepares removal of task comments by the task ids.
     *
     * @param taskIds the task ids
     * @return the transaction key
     */
    String prepareRemoveTaskCommentsByTaskIds(List<Long> taskIds);

    /**
     * 2PC commit phase, commits removal of task comments by transaction key returned by prepare method.
     *
     * @param transactionKey the transaction key
     */
    void commitRemoveTaskCommentsByTaskIds(String transactionKey);

    /**
     * 2PC rollback phase, rolls back removal of task comments by transaction key returned by prepare method.
     *
     * @param transactionKey the transaction key
     */
    void rollbackRemoveTaskCommentsByTaskIds(String transactionKey);
}
