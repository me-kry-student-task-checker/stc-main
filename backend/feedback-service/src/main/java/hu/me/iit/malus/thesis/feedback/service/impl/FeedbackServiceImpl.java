package hu.me.iit.malus.thesis.feedback.service.impl;

import hu.me.iit.malus.thesis.feedback.client.FileManagementClient;
import hu.me.iit.malus.thesis.feedback.client.NotificationClient;
import hu.me.iit.malus.thesis.feedback.client.UserClient;
import hu.me.iit.malus.thesis.feedback.client.dto.ActivitySaveDto;
import hu.me.iit.malus.thesis.feedback.client.dto.enums.ActivityType;
import hu.me.iit.malus.thesis.feedback.model.CourseComment;
import hu.me.iit.malus.thesis.feedback.model.TaskComment;
import hu.me.iit.malus.thesis.feedback.repository.CourseCommentRepository;
import hu.me.iit.malus.thesis.feedback.repository.TaskCommentRepository;
import hu.me.iit.malus.thesis.feedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final CourseCommentRepository courseCommentRepository;
    private final TaskCommentRepository taskCommentRepository;
    private final FileManagementClient fileManagementClient;
    private final UserClient userClient;
    private final NotificationClient notificationClient;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public CourseComment createCourseComment(CourseComment courseComment) {
        courseComment.setCreateDate(new Date());
        CourseComment savedComment = courseCommentRepository.save(courseComment);
        userClient.saveLastActivity(new ActivitySaveDto(ActivityType.FEEDBACK_COURSE));
        boolean doSendNotification = userClient.getNotificationPreferences().get(ActivityType.FEEDBACK_COURSE);
        if (doSendNotification) {
            notificationClient.sendNotification();
        }
        log.info("Created course comment: {}", savedComment);
        return savedComment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public TaskComment createTaskComment(TaskComment taskComment) {
        taskComment.setCreateDate(new Date());
        TaskComment savedComment = taskCommentRepository.save(taskComment);
        userClient.saveLastActivity(new ActivitySaveDto(ActivityType.FEEDBACK_TASK));
        boolean doSendNotification = userClient.getNotificationPreferences().get(ActivityType.FEEDBACK_TASK);
        if (doSendNotification) {
            notificationClient.sendNotification();
        }
        log.info("Created task comment: {}", savedComment);
        return savedComment;
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
