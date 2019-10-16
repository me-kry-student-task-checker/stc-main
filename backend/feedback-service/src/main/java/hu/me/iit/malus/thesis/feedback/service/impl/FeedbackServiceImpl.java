package hu.me.iit.malus.thesis.feedback.service.impl;

import hu.me.iit.malus.thesis.feedback.client.FileManagementClient;
import hu.me.iit.malus.thesis.feedback.model.CourseComment;
import hu.me.iit.malus.thesis.feedback.model.TaskComment;
import hu.me.iit.malus.thesis.feedback.repository.CourseCommentRepository;
import hu.me.iit.malus.thesis.feedback.repository.TaskCommentRepository;
import hu.me.iit.malus.thesis.feedback.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Default implementation of Feedback Service interface.
 *
 * @author Attila Szőke
 */
@Service
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {

    private CourseCommentRepository courseCommentRepository;
    private TaskCommentRepository taskCommentRepository;
    private FileManagementClient fileManagementClient;

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
                fileManagementClient.getAllFilesByTagId(hu.me.iit.malus.thesis.feedback.client.dto.Service.FEEDBACK,
                        courseComment.getId()).getBody()));

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
                fileManagementClient.getAllFilesByTagId(hu.me.iit.malus.thesis.feedback.client.dto.Service.FEEDBACK,
                        taskComment.getId()).getBody()));

        return results;
    }
}
