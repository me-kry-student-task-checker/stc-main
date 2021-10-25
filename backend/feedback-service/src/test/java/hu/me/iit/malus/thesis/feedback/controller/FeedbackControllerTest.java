package hu.me.iit.malus.thesis.feedback.controller;

import hu.me.iit.malus.thesis.feedback.controller.dto.CourseCommentCreateDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.CourseCommentDetailsDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.TaskCommentCreateDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.TaskCommentDetailsDto;
import hu.me.iit.malus.thesis.feedback.service.FeedbackService;
import hu.me.iit.malus.thesis.feedback.service.exception.CommentNotFoundException;
import hu.me.iit.malus.thesis.feedback.service.exception.ForbiddenCommentEditException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.security.Principal;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FeedbackControllerTest {

    @Mock
    private FeedbackService service;

    @Mock
    private Principal principal;

    @InjectMocks
    private FeedbackController controller;

    @Test
    public void createCourseComment() {
        String testText = "Rs3o1";
        long testCourseId = 323L;
        CourseCommentCreateDto dto = new CourseCommentCreateDto(testText, testCourseId);
        CourseCommentDetailsDto testDetailsDto = new CourseCommentDetailsDto();
        String testPrincipalName = "BESmrtaE";
        when(principal.getName()).thenReturn(testPrincipalName);
        when(service.createCourseComment(dto, testPrincipalName)).thenReturn(testDetailsDto);

        CourseCommentDetailsDto result = controller.create(dto, principal);

        assertThat(result, is(testDetailsDto));
        verify(principal).getName();
        verify(service).createCourseComment(dto, principal.getName());
    }

    @Test
    public void createTaskComment() {
        String testText = "Fx5l5";
        long testCourseId = 924L;
        TaskCommentCreateDto dto = new TaskCommentCreateDto(testText, testCourseId);
        TaskCommentDetailsDto testDetailsDto = new TaskCommentDetailsDto();
        String testPrincipalName = "X3AO";
        when(principal.getName()).thenReturn(testPrincipalName);
        when(service.createTaskComment(dto, testPrincipalName)).thenReturn(testDetailsDto);

        TaskCommentDetailsDto result = controller.create(dto, principal);

        assertThat(result, is(testDetailsDto));
        verify(principal).getName();
        verify(service).createTaskComment(dto, principal.getName());
    }

    @Test
    public void getAllCourseComments() {
        List<CourseCommentDetailsDto> testList = List.of();
        long testCourseId = 938L;
        when(service.getAllCourseComments(testCourseId)).thenReturn(testList);

        List<CourseCommentDetailsDto> result = controller.getAllCourseComments(testCourseId);

        assertThat(result, is(testList));
        verify(service).getAllCourseComments(testCourseId);
    }

    @Test
    public void getAllTaskComments() {
        List<TaskCommentDetailsDto> testList = List.of();
        long testTaskId = 938L;
        when(service.getAllTaskComments(testTaskId)).thenReturn(testList);

        List<TaskCommentDetailsDto> result = controller.getAllTaskComments(testTaskId);

        assertThat(result, is(testList));
        verify(service).getAllTaskComments(testTaskId);
    }

    @Test
    public void deleteCourseComment() throws Exception {
        long commentId = 107L;
        String testPrincipalName = "M01";
        when(principal.getName()).thenReturn(testPrincipalName);
        doNothing().when(service).removeCourseComment(commentId, testPrincipalName);

        controller.deleteCourseComment(commentId, principal);

        verify(principal).getName();
        verify(service).removeCourseComment(commentId, principal.getName());
    }

    @Test(expected = CommentNotFoundException.class)
    public void deleteCourseComment_throwsNotFoundException() throws Exception {
        long commentId = 107L;
        String testPrincipalName = "M01";
        when(principal.getName()).thenReturn(testPrincipalName);
        doThrow(CommentNotFoundException.class).when(service).removeCourseComment(commentId, testPrincipalName);

        controller.deleteCourseComment(commentId, principal);
    }

    @Test(expected = ForbiddenCommentEditException.class)
    public void deleteCourseComment_throwsForbiddenException() throws Exception {
        long commentId = 107L;
        String testPrincipalName = "M01";
        when(principal.getName()).thenReturn(testPrincipalName);
        doThrow(ForbiddenCommentEditException.class).when(service).removeCourseComment(commentId, testPrincipalName);

        controller.deleteCourseComment(commentId, principal);
    }

    @Test
    public void deleteTaskComment() throws Exception {
        long commentId = 577L;
        String testPrincipalName = "26M86cho";
        when(principal.getName()).thenReturn(testPrincipalName);
        doNothing().when(service).removeTaskComment(commentId, testPrincipalName);

        controller.deleteTaskComment(commentId, principal);

        verify(principal).getName();
        verify(service).removeTaskComment(commentId, principal.getName());
    }

    @Test(expected = ForbiddenCommentEditException.class)
    public void deleteTaskComment_throwsForbiddenException() throws Exception {
        long commentId = 577L;
        String testPrincipalName = "26M86cho";
        when(principal.getName()).thenReturn(testPrincipalName);
        doThrow(ForbiddenCommentEditException.class).when(service).removeTaskComment(commentId, testPrincipalName);

        controller.deleteTaskComment(commentId, principal);

        verify(principal).getName();
        verify(service).removeTaskComment(commentId, principal.getName());
    }

    @Test(expected = CommentNotFoundException.class)
    public void deleteTaskComment_throwsNotFoundException() throws Exception {
        long commentId = 577L;
        String testPrincipalName = "26M86cho";
        when(principal.getName()).thenReturn(testPrincipalName);
        doThrow(CommentNotFoundException.class).when(service).removeTaskComment(commentId, testPrincipalName);

        controller.deleteTaskComment(commentId, principal);

        verify(principal).getName();
        verify(service).removeTaskComment(commentId, principal.getName());
    }

    @Test
    public void removeCourseCommentsByCourseId() {
        long courseId = 187L;
        doNothing().when(service).removeCourseCommentsByCourseId(courseId);

        controller.removeCourseCommentsByCourseId(courseId);

        verify(service).removeCourseCommentsByCourseId(courseId);
    }

    @Test
    public void removeTaskCommentsByTaskId() {
        long taskId = 469L;
        doNothing().when(service).removeTaskCommentsByTaskId(taskId);

        controller.removeTaskCommentsByTaskId(taskId);

        verify(service).removeTaskCommentsByTaskId(taskId);
    }
}