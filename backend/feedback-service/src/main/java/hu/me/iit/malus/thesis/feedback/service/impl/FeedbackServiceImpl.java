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

    @Autowired
    public FeedbackServiceImpl(CourseCommentRepository courseCommentRepository, TaskCommentRepository taskCommentRepository) {
        this.courseCommentRepository = courseCommentRepository;
        this.taskCommentRepository = taskCommentRepository;
    }

    @Override
    public CourseComment createCourseComment(CourseComment courseComment) {
        return courseCommentRepository.save(courseComment);
    }

    @Override
    public TaskComment createTaskComment(TaskComment taskComment) {
        return taskCommentRepository.save(taskComment);
    }

    @Override
    public List<CourseComment> getAllCourseComments(Long courseId) {
        Optional<List<CourseComment>> opt = courseCommentRepository.findAllByCourseId(courseId);
        return opt.orElseGet(ArrayList::new);
    }

    @Override
    public List<TaskComment> getAllTaskComments(Long taskId) {
        Optional<List<TaskComment>> opt = taskCommentRepository.findAllByTaskId(taskId);
        return opt.orElseGet(ArrayList::new);
    }
}
