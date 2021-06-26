package hu.me.iit.malus.thesis.feedback.service.impl;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
        List<CourseComment> results = courseCommentRepository.findAllByCourseId(courseId);
        results.forEach(courseComment -> courseComment.setFiles(
                fileManagementClient.getAllFilesByTagId(hu.me.iit.malus.thesis.dto.Service.FEEDBACK, courseComment.getId()))
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
        List<TaskComment> results = taskCommentRepository.findAllByTaskId(taskId);
        results.forEach(taskComment -> taskComment.setFiles(
                fileManagementClient.getAllFilesByTagId(hu.me.iit.malus.thesis.dto.Service.FEEDBACK, taskComment.getId()))
        );
        log.debug("Listing comments for task id: {}", taskId);
        return results.stream().map(DtoConverter::taskCommentToTaskCommentDetailsDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeCourseComment(Long commentId, String authorId) throws CommentNotFoundException, ForbiddenCommentEditException {
        CourseComment courseComment = courseCommentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if (!courseComment.getAuthorId().equals(authorId)) {
            throw new ForbiddenCommentEditException();
        }
        courseCommentRepository.delete(courseComment);
        fileManagementClient.removeFilesByServiceAndTagId(hu.me.iit.malus.thesis.dto.Service.FEEDBACK, courseComment.getId());
    }

    @Override
    @Transactional
    public void removeTaskComment(Long commentId, String authorId) throws CommentNotFoundException, ForbiddenCommentEditException {
        TaskComment taskComment = taskCommentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if (!taskComment.getAuthorId().equals(authorId)) {
            throw new ForbiddenCommentEditException();
        }
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
