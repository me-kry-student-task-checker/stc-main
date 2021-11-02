package hu.me.iit.malus.thesis.feedback.service.impl;

import hu.me.iit.malus.thesis.dto.ServiceType;
import hu.me.iit.malus.thesis.feedback.client.FileManagementClient;
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
import hu.me.iit.malus.thesis.feedback.service.exception.CommentNotFoundException;
import hu.me.iit.malus.thesis.feedback.service.exception.ForbiddenCommentEditException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
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
    private final RedisTemplate<String, List<Long>> redisTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    public CourseCommentDetailsDto createCourseComment(CourseCommentCreateDto dto, String authorId) {
        CourseComment newComment = DtoConverter.courseCommentCreateDtoToCourseComment(dto);
        newComment.setCreateDate(new Date());
        newComment.setAuthorId(authorId);
        newComment = courseCommentRepository.save(newComment);
        log.debug("Created course comment: {}", newComment);
        return DtoConverter.courseCommentToCourseCommentDetailsDto(newComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskCommentDetailsDto createTaskComment(TaskCommentCreateDto dto, String authorId) {
        TaskComment newComment = DtoConverter.taskCommentCreateDtoToTaskComment(dto);
        newComment.setCreateDate(new Date());
        newComment.setAuthorId(authorId);
        newComment = taskCommentRepository.save(newComment);
        log.debug("Created task comment: {}", newComment);
        return DtoConverter.taskCommentToTaskCommentDetailsDto(newComment);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List<CourseCommentDetailsDto> getAllCourseComments(Long courseId) {
        List<CourseComment> results = courseCommentRepository.findAllByCourseIdAndRemovedFalse(courseId);
        results.forEach(courseComment -> courseComment.setFiles(
                fileManagementClient.getAllFilesByTagId(ServiceType.FEEDBACK, courseComment.getId()))
        );
        log.debug("Listing comments for course id: {}", courseId);
        return results.stream().map(DtoConverter::courseCommentToCourseCommentDetailsDto).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List<TaskCommentDetailsDto> getAllTaskComments(Long taskId) {
        List<TaskComment> results = taskCommentRepository.findAllByTaskIdAndRemovedFalse(taskId);
        results.forEach(taskComment -> taskComment.setFiles(
                fileManagementClient.getAllFilesByTagId(ServiceType.FEEDBACK, taskComment.getId()))
        );
        log.debug("Listing comments for task id: {}", taskId);
        return results.stream().map(DtoConverter::taskCommentToTaskCommentDetailsDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeCourseComment(Long commentId, String authorId) throws CommentNotFoundException, ForbiddenCommentEditException {
        CourseComment courseComment = courseCommentRepository.findByIdAndRemovedFalse(commentId).orElseThrow(CommentNotFoundException::new);
        if (!courseComment.getAuthorId().equals(authorId)) {
            throw new ForbiddenCommentEditException();
        }
        courseComment.setRemoved(true);
        courseCommentRepository.save(courseComment);
        fileManagementClient.removeFilesByServiceAndTagId(ServiceType.FEEDBACK, courseComment.getId());
    }

    @Override
    @Transactional
    public void removeTaskComment(Long commentId, String authorId) throws CommentNotFoundException, ForbiddenCommentEditException {
        TaskComment taskComment = taskCommentRepository.findByIdAndRemovedFalse(commentId).orElseThrow(CommentNotFoundException::new);
        if (!taskComment.getAuthorId().equals(authorId)) {
            throw new ForbiddenCommentEditException();
        }
        taskComment.setRemoved(true);
        taskCommentRepository.save(taskComment);
        fileManagementClient.removeFilesByServiceAndTagId(ServiceType.FEEDBACK, taskComment.getId());
    }

    @Override
    @Transactional
    public String prepareRemoveCourseCommentsByCourseId(Long courseId) {
        List<CourseComment> courseComments = courseCommentRepository.findAllByCourseIdAndRemovedFalse(courseId);
        courseComments.forEach(courseComment -> courseComment.setRemoved(true));
        courseCommentRepository.saveAll(courseComments);
        String uuid = UUID.randomUUID().toString();
        List<Long> courseCommentIds = courseComments.stream().map(CourseComment::getId).collect(Collectors.toList());
        redisTemplate.opsForValue().set(uuid, courseCommentIds);
        log.info("Prepared ids: {}, for removal with {} transaction key!", courseCommentIds, uuid);
        return uuid;
    }

    @Override
    public void commitRemoveCourseCommentsByCourseId(String transactionKey) {
        boolean success = redisTemplate.delete(transactionKey);
        log.info("Committed transaction with key: {}, delete successful: {}!", transactionKey, success);
    }

    @Override
    @Transactional
    public void rollbackRemoveCourseCommentsByCourseId(String transactionKey) {
        List<Long> courseCommentIds = redisTemplate.opsForValue().get(transactionKey);
        if (courseCommentIds == null) {
            log.info("Cannot find transaction key in Redis, like this: '{}'!", transactionKey);
            return;
        }
        List<CourseComment> courseComments = courseCommentRepository.findAllById(courseCommentIds);
        courseComments.forEach(task -> task.setRemoved(false));
        courseCommentRepository.saveAll(courseComments);
        redisTemplate.delete(transactionKey);
        log.info("Rolled back transaction with key: {}!", transactionKey);
    }

    @Override
    @Transactional
    public String prepareRemoveTaskCommentsByTaskIds(List<Long> taskIds) {
        List<TaskComment> taskComments = taskCommentRepository.findAllByTaskIdInAndRemovedFalse(taskIds);
        taskComments.forEach(taskComment -> taskComment.setRemoved(true));
        taskCommentRepository.saveAll(taskComments);
        String uuid = UUID.randomUUID().toString();
        List<Long> taskCommentIds = taskComments.stream().map(TaskComment::getId).collect(Collectors.toList());
        redisTemplate.opsForValue().set(uuid, taskCommentIds);
        log.info("Prepared ids: {}, for removal with {} transaction key!", taskCommentIds, uuid);
        return uuid;
    }

    @Override
    public void commitRemoveTaskCommentsByTaskIds(String transactionKey) {
        boolean success = redisTemplate.delete(transactionKey);
        log.info("Committed transaction with key: {}, Redis delete successful: {}!", transactionKey, success);
    }

    @Override
    @Transactional
    public void rollbackRemoveTaskCommentsByTaskIds(String transactionKey) {
        List<Long> taskCommentIds = redisTemplate.opsForValue().get(transactionKey);
        if (taskCommentIds == null) {
            log.info("Cannot find transaction key in Redis, like this: '{}'!", transactionKey);
            return;
        }
        List<TaskComment> taskComments = taskCommentRepository.findAllById(taskCommentIds);
        taskComments.forEach(task -> task.setRemoved(false));
        taskCommentRepository.saveAll(taskComments);
        redisTemplate.delete(transactionKey);
        log.info("Rolled back transaction with key: {}!", transactionKey);
    }
}
