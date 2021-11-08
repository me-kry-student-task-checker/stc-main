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
    public void prepareRemoveCourseCommentsByCourseId() {
    	long testCourseId = 938L;
    	String testUuid = "046b6c7f-0b8a-43b9-b35d-6489e6daee91";
    	
    	when(service.prepareRemoveCourseCommentsByCourseId(testCourseId)).thenReturn(testUuid);
    	
    	String result = controller.prepareRemoveCourseCommentsByCourseId(testCourseId);
    	
    	assertThat(testUuid , is(result));
    	verify(service).prepareRemoveCourseCommentsByCourseId(testCourseId);
    }
    
    @Test
    public void commitRemoveCourseCommentsByCourseId() {
    	String transactionKey = "046b6c7f-0b8a-43b9-b35d-6489e6daee91";
    	doNothing().when(service).commitRemoveCourseCommentsByCourseId(transactionKey);
    	
    	controller.commitRemoveCourseCommentsByCourseId(transactionKey);
    	
    	verify(service).commitRemoveCourseCommentsByCourseId(transactionKey);
    }
    
    @Test
    public void rollbackRemoveCourseCommentsByCourseId() {
    	String transactionKey = "046b6c7f-0b8a-43b9-b35d-6489e6daee91";
    	doNothing().when(service).rollbackRemoveCourseCommentsByCourseId(transactionKey);
    	
    	controller.rollbackRemoveCourseCommentsByCourseId(transactionKey);
    	
    	verify(service).rollbackRemoveCourseCommentsByCourseId(transactionKey);
    }
    
    @Test
    public void prepareRemoveTaskCommentsByTaskIds() {
    	List<Long> testList = List.of();
    	String testUuid = "046b6c7f-0b8a-43b9-b35d-6489e6daee91";
    	when(service.prepareRemoveTaskCommentsByTaskIds(testList)).thenReturn(testUuid);
    	
    	String result = controller.prepareRemoveTaskCommentsByTaskIds(testList);
    	
    	assertThat(testUuid, is(result));
    	verify(service).prepareRemoveTaskCommentsByTaskIds(testList);
    }
    
    @Test
    public void commitRemoveTaskCommentsByTaskIds() {
    	String testTransactionKey = "046b6c7f-0b8a-43b9-b35d-6489e6daee91";
    	doNothing().when(service).commitRemoveTaskCommentsByTaskIds(testTransactionKey);
    	
    	controller.commitRemoveTaskCommentsByTaskIds(testTransactionKey);
    	
    	verify(service).commitRemoveTaskCommentsByTaskIds(testTransactionKey);
    }
    
    @Test
    public void rollbackRemoveTaskCommentsByTaskIds() {
    	String testTransactionKey = "046b6c7f-0b8a-43b9-b35d-6489e6daee91";
    	doNothing().when(service).rollbackRemoveTaskCommentsByTaskIds(testTransactionKey);
    	
    	controller.rollbackRemoveTaskCommentsByTaskIds(testTransactionKey);
    	
    	verify(service).rollbackRemoveTaskCommentsByTaskIds(testTransactionKey);
    }

}