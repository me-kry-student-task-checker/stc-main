package hu.me.iit.malus.thesis.feedback.service.impl;

import hu.me.iit.malus.thesis.dto.Service;
import hu.me.iit.malus.thesis.feedback.client.FileManagementClient;
import hu.me.iit.malus.thesis.feedback.controller.dto.CourseCommentCreateDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.CourseCommentDetailsDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.TaskCommentCreateDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.TaskCommentDetailsDto;
import hu.me.iit.malus.thesis.feedback.model.CourseComment;
import hu.me.iit.malus.thesis.feedback.model.TaskComment;
import hu.me.iit.malus.thesis.feedback.repository.CourseCommentRepository;
import hu.me.iit.malus.thesis.feedback.repository.TaskCommentRepository;
import hu.me.iit.malus.thesis.feedback.service.converters.DtoConverter;
import hu.me.iit.malus.thesis.feedback.service.exception.CommentNotFoundException;
import hu.me.iit.malus.thesis.feedback.service.exception.ForbiddenCommentEditException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FeedbackServiceImplTest {

    @Mock
    private CourseCommentRepository courseCommentRepository;

    @Mock
    private TaskCommentRepository taskCommentRepository;

    @Mock
    private FileManagementClient fileManagementClient;

    @InjectMocks
    private FeedbackServiceImpl service;

    @Test
    public void createCourseComment() {
        String testText = "KyuB9ZN6";
        long testCourseId = 419L;
        CourseCommentCreateDto dto = new CourseCommentCreateDto(testText, testCourseId);
        CourseComment shouldBeComment = DtoConverter.courseCommentCreateDtoToCourseComment(dto);
        Date testDate = new Date();
        String testAuthor = "uFB";
        shouldBeComment.setCreateDate(testDate);
        shouldBeComment.setAuthorId(testAuthor);
        when(courseCommentRepository.save(shouldBeComment)).thenReturn(shouldBeComment);

        CourseCommentDetailsDto result = service.createCourseComment(dto, testAuthor);

        assertThat(result, is(DtoConverter.courseCommentToCourseCommentDetailsDto(shouldBeComment)));
        verify(courseCommentRepository).save(shouldBeComment);
    }

    @Test
    public void createTaskComment() {
        String testText = "V4S4hA6";
        long testTaskId = 188L;
        TaskCommentCreateDto dto = new TaskCommentCreateDto(testText, testTaskId);
        TaskComment shouldBeComment = DtoConverter.taskCommentCreateDtoToTaskComment(dto);
        Date testDate = new Date();
        String testAuthor = "Ha04A";
        shouldBeComment.setCreateDate(testDate);
        shouldBeComment.setAuthorId(testAuthor);
        when(taskCommentRepository.save(shouldBeComment)).thenReturn(shouldBeComment);

        TaskCommentDetailsDto result = service.createTaskComment(dto, testAuthor);

        assertThat(result, is(DtoConverter.taskCommentToTaskCommentDetailsDto(shouldBeComment)));
        verify(taskCommentRepository).save(shouldBeComment);
    }

    @Test
    public void getAllCourseComments() {
        String testText = "KyuB9ZN6";
        long testCourseId = 419L;
        CourseCommentCreateDto dto = new CourseCommentCreateDto(testText, testCourseId);
        CourseComment shouldBeComment = DtoConverter.courseCommentCreateDtoToCourseComment(dto);
        Long testId = 731L;
        Date testDate = new Date();
        String testAuthor = "uFB";
        shouldBeComment.setId(testId);
        shouldBeComment.setCreateDate(testDate);
        shouldBeComment.setAuthorId(testAuthor);
        List<CourseComment> testList = List.of(shouldBeComment, shouldBeComment);
        when(courseCommentRepository.findAllByCourseId(testCourseId)).thenReturn(testList);
        when(fileManagementClient.getAllFilesByTagId(Service.FEEDBACK, testId)).thenReturn(Set.of());

        List<CourseCommentDetailsDto> result = service.getAllCourseComments(testCourseId);

        assertThat(result, is(testList.stream().map(DtoConverter::courseCommentToCourseCommentDetailsDto).collect(Collectors.toList())));
        verify(courseCommentRepository).findAllByCourseId(testCourseId);
        verify(fileManagementClient, times(2)).getAllFilesByTagId(Service.FEEDBACK, testId);
    }

    @Test
    public void getAllTaskComments() {
        String testText = "P49J4F";
        long testTaskId = 389L;
        TaskCommentCreateDto dto = new TaskCommentCreateDto(testText, testTaskId);
        TaskComment shouldBeComment = DtoConverter.taskCommentCreateDtoToTaskComment(dto);
        Long testId = 247L;
        Date testDate = new Date();
        String testAuthor = "p541c5";
        shouldBeComment.setId(testId);
        shouldBeComment.setCreateDate(testDate);
        shouldBeComment.setAuthorId(testAuthor);
        List<TaskComment> testList = List.of(shouldBeComment, shouldBeComment);
        when(taskCommentRepository.findAllByTaskId(testTaskId)).thenReturn(testList);
        when(fileManagementClient.getAllFilesByTagId(Service.FEEDBACK, testId)).thenReturn(Set.of());

        List<TaskCommentDetailsDto> result = service.getAllTaskComments(testTaskId);

        assertThat(result, is(testList.stream().map(DtoConverter::taskCommentToTaskCommentDetailsDto).collect(Collectors.toList())));
        verify(taskCommentRepository).findAllByTaskId(testTaskId);
        verify(fileManagementClient, times(2)).getAllFilesByTagId(Service.FEEDBACK, testId);

    }

    @Test
    public void removeCourseComment() throws Exception {
        long testId = 761L;
        String testAuthorId = "aifDTs5g";
        CourseComment courseComment = new CourseComment();
        courseComment.setId(testId);
        courseComment.setAuthorId(testAuthorId);
        when(courseCommentRepository.findById(testId)).thenReturn(Optional.of(courseComment));
        doNothing().when(courseCommentRepository).delete(courseComment);
        doNothing().when(fileManagementClient).removeFilesByServiceAndTagId(Service.FEEDBACK, testId);

        service.removeCourseComment(testId, testAuthorId);

        verify(courseCommentRepository).findById(testId);
        verify(courseCommentRepository).delete(courseComment);
        verify(fileManagementClient).removeFilesByServiceAndTagId(Service.FEEDBACK, testId);
    }

    @Test(expected = CommentNotFoundException.class)
    public void removeCourseComment_commentNotFoundExceptionThrown() throws Exception {
        long testId = 355L;
        String testAuthorId = "263JLx";
        when(courseCommentRepository.findById(testId)).thenReturn(Optional.empty());

        service.removeCourseComment(testId, testAuthorId);
    }

    @Test(expected = ForbiddenCommentEditException.class)
    public void removeCourseComment_forbiddenCommentEditExceptionThrown() throws Exception {
        long testId = 298L;
        String testAuthorId = "v76iY";
        CourseComment courseComment = new CourseComment();
        courseComment.setId(testId);
        courseComment.setAuthorId(testAuthorId);
        when(courseCommentRepository.findById(testId)).thenReturn(Optional.of(courseComment));

        service.removeCourseComment(testId, "FinE7YKR");
    }

    @Test
    public void removeTaskComment() throws Exception {
        long testId = 512L;
        String testAuthor = "kp550b5";
        TaskComment taskComment = new TaskComment();
        taskComment.setId(testId);
        taskComment.setAuthorId(testAuthor);
        when(taskCommentRepository.findById(testId)).thenReturn(Optional.of(taskComment));
        doNothing().when(taskCommentRepository).delete(taskComment);
        doNothing().when(fileManagementClient).removeFilesByServiceAndTagId(Service.FEEDBACK, testId);

        service.removeTaskComment(testId, testAuthor);

        verify(taskCommentRepository).findById(testId);
        verify(taskCommentRepository).delete(taskComment);
        verify(fileManagementClient).removeFilesByServiceAndTagId(Service.FEEDBACK, testId);
    }

    @Test(expected = CommentNotFoundException.class)
    public void removeTaskComment_commentNotFoundExceptionThrown() throws Exception {
        long testId = 483L;
        String testAuthorId = "pAY";
        when(taskCommentRepository.findById(testId)).thenReturn(Optional.empty());

        service.removeTaskComment(testId, testAuthorId);
    }

    @Test(expected = ForbiddenCommentEditException.class)
    public void removeTaskComment_forbiddenCommentEditExceptionThrown() throws Exception {
        long testId = 931L;
        String testAuthorId = "wTMfsa";
        TaskComment taskComment = new TaskComment();
        taskComment.setId(testId);
        taskComment.setAuthorId(testAuthorId);
        when(taskCommentRepository.findById(testId)).thenReturn(Optional.of(taskComment));

        service.removeTaskComment(testId, "9SDL");
    }

    @Test
    public void removeFeedbacksByCourseId() {
        long testCourseId = 598L;
        CourseComment courseComment = new CourseComment();
        courseComment.setId(testCourseId);
        List<CourseComment> testList = List.of(courseComment, courseComment, courseComment);
        when(courseCommentRepository.deleteByCourseId(testCourseId)).thenReturn(testList);
        doNothing().when(fileManagementClient).removeFilesByServiceAndTagId(Service.FEEDBACK, courseComment.getId());

        service.removeFeedbacksByCourseId(testCourseId);

        verify(courseCommentRepository).deleteByCourseId(testCourseId);
        verify(fileManagementClient, times(3)).removeFilesByServiceAndTagId(Service.FEEDBACK, testCourseId);
    }

    @Test
    public void removeFeedbacksByTaskId() {
        long testTaskId = 235L;
        TaskComment taskComment = new TaskComment();
        taskComment.setId(testTaskId);
        List<TaskComment> testList = List.of(taskComment, taskComment, taskComment);
        when(taskCommentRepository.deleteByTaskId(testTaskId)).thenReturn(testList);
        doNothing().when(fileManagementClient).removeFilesByServiceAndTagId(Service.FEEDBACK, taskComment.getId());

        service.removeFeedbacksByTaskId(testTaskId);

        verify(taskCommentRepository).deleteByTaskId(testTaskId);
        verify(fileManagementClient, times(3)).removeFilesByServiceAndTagId(Service.FEEDBACK, testTaskId);
    }
}