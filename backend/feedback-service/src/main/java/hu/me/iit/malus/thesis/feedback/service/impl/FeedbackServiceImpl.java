package hu.me.iit.malus.thesis.feedback.service.impl;

import hu.me.iit.malus.thesis.feedback.model.CourseComment;
import hu.me.iit.malus.thesis.feedback.model.TaskComment;
import hu.me.iit.malus.thesis.feedback.repository.CourseCommentRepository;
import hu.me.iit.malus.thesis.feedback.repository.TaskCommentRepository;
import hu.me.iit.malus.thesis.feedback.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private CourseCommentRepository courseCommentRepository;
    private TaskCommentRepository taskCommentRepository;

    /**
     * Instantiates a new FeedbackServiceImpl
     */
    @Autowired
    public FeedbackServiceImpl(CourseCommentRepository courseCommentRepository, TaskCommentRepository taskCommentRepository) {
        this.courseCommentRepository = courseCommentRepository;
        this.taskCommentRepository = taskCommentRepository;
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
        return opt.orElseGet(ArrayList::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskComment> getAllTaskComments(Long taskId) {
        log.info("Listing comments for task id: {}", taskId);
        Optional<List<TaskComment>> opt = taskCommentRepository.findAllByTaskId(taskId);
        return opt.orElseGet(ArrayList::new);
    }
}