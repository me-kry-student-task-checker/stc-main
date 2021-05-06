package hu.me.iit.malus.thesis.feedback.service.impl;

import hu.me.iit.malus.thesis.feedback.client.FileManagementClient;
import hu.me.iit.malus.thesis.feedback.model.CourseComment;
import hu.me.iit.malus.thesis.feedback.model.TaskComment;
import hu.me.iit.malus.thesis.feedback.repository.CourseCommentRepository;
import hu.me.iit.malus.thesis.feedback.repository.TaskCommentRepository;
import hu.me.iit.malus.thesis.feedback.service.FeedbackService;
import hu.me.iit.malus.thesis.feedback.service.exception.CommentNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of Feedback Service interface.
 *
 * @author Attila Szőke
 */
@Service
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {

    private final CourseCommentRepository courseCommentRepository;
    private final TaskCommentRepository taskCommentRepository;
    private final FileManagementClient fileManagementClient;

    /**
     * Instantiates a new FeedbackServiceImpl
     */
    @Autowired
    public FeedbackServiceImpl(CourseCommentRepository courseCommentRepository, TaskCommentRepository taskCommentRepository,
                               FileManagementClient fileManagementClient) {
        this.courseCommentRepository = courseCommentRepository;
        this.taskCommentRepository = taskCommentRepository;
        this.fileManagementClient = fileManagementClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CourseComment createCourseComment(CourseComment courseComment) {
        log.info("Created course comment: {}", courseComment);
        courseComment.setCreateDate(new Date());
        return courseCommentRepository.save(courseComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskComment createTaskComment(TaskComment taskComment) {
        log.info("Created task comment: {}", taskComment);
        taskComment.setCreateDate(new Date());
        return taskCommentRepository.save(taskComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CourseComment> getAllCourseComments(Long courseId) {
        log.info("Listing comments for course id: {}", courseId);
        Optional<List<CourseComment>> opt = courseCommentRepository.findAllByCourseId(courseId);
        List<CourseComment> results = opt.orElseGet(ArrayList::new);
        results.forEach(courseComment -> courseComment.setFiles(
                fileManagementClient.getAllFilesByTagId(hu.me.iit.malus.thesis.dto.Service.FEEDBACK,
                        courseComment.getId())));

        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskComment> getAllTaskComments(Long taskId) {
        log.info("Listing comments for task id: {}", taskId);
        Optional<List<TaskComment>> opt = taskCommentRepository.findAllByTaskId(taskId);
        List<TaskComment> results = opt.orElseGet(ArrayList::new);
        results.forEach(taskComment -> taskComment.setFiles(
                fileManagementClient.getAllFilesByTagId(hu.me.iit.malus.thesis.dto.Service.FEEDBACK,
                        taskComment.getId())));

        return results;
    }

    @Override
    @Transactional
    public void removeCourseComment(Long commentId) throws CommentNotFoundException {
        CourseComment courseComment = courseCommentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        courseCommentRepository.delete(courseComment);
        fileManagementClient.removeFilesByServiceAndTagId(hu.me.iit.malus.thesis.dto.Service.FEEDBACK, courseComment.getId());
    }

    @Override
    @Transactional
    public void removeTaskComment(Long commentId) throws CommentNotFoundException {
        TaskComment taskComment = taskCommentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        taskCommentRepository.delete(taskComment);
        fileManagementClient.removeFilesByServiceAndTagId(hu.me.iit.malus.thesis.dto.Service.FEEDBACK, taskComment.getId());
    }

    @Override
    @Transactional
    public void removeFeedbacksByCourseId(Long courseId) {
        List<CourseComment> courseComments = courseCommentRepository.deleteByCourseId(courseId);
        courseComments.forEach(
                courseComment -> fileManagementClient.removeFilesByServiceAndTagId(hu.me.iit.malus.thesis.dto.Service.FEEDBACK, courseComment.getId())
        );
    }

    @Override
    @Transactional
    public void removeFeedbacksByTaskId(Long taskId) {
        List<TaskComment> taskComments = taskCommentRepository.deleteByTaskId(taskId);
        taskComments.forEach(
                taskComment -> fileManagementClient.removeFilesByServiceAndTagId(hu.me.iit.malus.thesis.dto.Service.FEEDBACK, taskComment.getId())
        );
    }
}
