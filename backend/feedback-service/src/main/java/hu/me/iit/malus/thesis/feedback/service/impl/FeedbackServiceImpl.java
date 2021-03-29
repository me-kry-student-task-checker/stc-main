package hu.me.iit.malus.thesis.feedback.service.impl;

import hu.me.iit.malus.thesis.feedback.client.FileManagementClient;
import hu.me.iit.malus.thesis.feedback.client.NotificationClient;
import hu.me.iit.malus.thesis.feedback.client.UserClient;
import hu.me.iit.malus.thesis.feedback.client.dto.ActivitySaveDto;
import hu.me.iit.malus.thesis.feedback.client.dto.enums.ActivityType;
import hu.me.iit.malus.thesis.feedback.controller.dto.CourseCommentCreateDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.CourseCommentDetailsDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.TaskCommentCreateDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.TaskCommentDetailsDto;
import hu.me.iit.malus.thesis.feedback.model.CourseComment;
import hu.me.iit.malus.thesis.feedback.model.TaskComment;
import hu.me.iit.malus.thesis.feedback.repository.CourseCommentRepository;
import hu.me.iit.malus.thesis.feedback.repository.TaskCommentRepository;
import hu.me.iit.malus.thesis.feedback.service.FeedbackService;
import hu.me.iit.malus.thesis.feedback.service.converters.DtoConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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
    public CourseCommentDetailsDto createCourseComment(CourseCommentCreateDto dto, String authorId) {
        CourseComment savedComment = DtoConverter.courseCommentCreateDtoToCourseComment(dto);
        savedComment.setAuthorId(authorId);
        savedComment = courseCommentRepository.save(savedComment);
        userClient.saveLastActivity(new ActivitySaveDto(ActivityType.FEEDBACK_COURSE, dto.getCourseId()));
        boolean doSendNotification = userClient.getNotificationPreferences().get(ActivityType.FEEDBACK_COURSE);
        if (doSendNotification) {
            notificationClient.sendNotification();
        }
        log.debug("Created course comment: {}", savedComment);
        return DtoConverter.courseCommentToCourseCommentDetailsDto(savedComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public TaskCommentDetailsDto createTaskComment(TaskCommentCreateDto dto, String authorId) {
        TaskComment savedComment = DtoConverter.taskCommentCreateDtoToTaskComment(dto);
        savedComment.setAuthorId(authorId);
        savedComment = taskCommentRepository.save(savedComment);
        userClient.saveLastActivity(new ActivitySaveDto(ActivityType.FEEDBACK_TASK, dto.getTaskId()));
        boolean doSendNotification = userClient.getNotificationPreferences().get(ActivityType.FEEDBACK_TASK);
        if (doSendNotification) {
            notificationClient.sendNotification();
        }
        log.debug("Created task comment: {}", savedComment);
        return DtoConverter.taskCommentToTaskCommentDetailsDto(savedComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CourseCommentDetailsDto> getAllCourseComments(Long courseId) {
        List<CourseComment> comments = courseCommentRepository.findAllByCourseId(courseId);
        comments.forEach(courseComment -> courseComment.setFiles(fileManagementClient
                .getAllFilesByTagId(hu.me.iit.malus.thesis.feedback.client.dto.Service.FEEDBACK, courseComment.getId()).getBody()));
        log.debug("Listed comments for course id: {}", courseId);
        return comments.stream().map(DtoConverter::courseCommentToCourseCommentDetailsDto).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskCommentDetailsDto> getAllTaskComments(Long taskId) {
        List<TaskComment> comments = taskCommentRepository.findAllByTaskId(taskId);
        comments.forEach(taskComment -> taskComment.setFiles(fileManagementClient
                .getAllFilesByTagId(hu.me.iit.malus.thesis.feedback.client.dto.Service.FEEDBACK, taskComment.getId()).getBody()));
        log.debug("Listed comments for task id: {}", taskId);
        return comments.stream().map(DtoConverter::taskCommentToTaskCommentDetailsDto).collect(Collectors.toList());
    }
}
