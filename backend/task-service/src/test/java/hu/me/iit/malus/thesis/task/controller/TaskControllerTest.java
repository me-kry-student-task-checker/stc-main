package hu.me.iit.malus.thesis.task.controller;

import hu.me.iit.malus.thesis.task.controller.dto.BriefTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.CreateTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.DetailedTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.EditTaskDto;
import hu.me.iit.malus.thesis.task.service.exception.StudentIdNotFoundException;
import hu.me.iit.malus.thesis.task.service.exception.TaskNotFoundException;
import hu.me.iit.malus.thesis.task.service.impl.TaskServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.security.Principal;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TaskControllerTest {

    @Mock
    private TaskServiceImpl service;

    @Mock
    private Principal principal;

    @InjectMocks
    private TaskController controller;

    @Test
    public void createTask() {
        CreateTaskDto createTaskDto = new CreateTaskDto();
        BriefTaskDto briefTaskDto = new BriefTaskDto();
        when(service.create(createTaskDto)).thenReturn(briefTaskDto);

        BriefTaskDto dto = controller.createTask(createTaskDto);

        assertThat(dto, is(briefTaskDto));
        verify(service).create(createTaskDto);
    }

    @Test
    public void editTask() throws Exception {
        String studentId = "Z8IMlf0";
        EditTaskDto editTaskDto = new EditTaskDto();
        BriefTaskDto briefTaskDto = new BriefTaskDto();
        when(principal.getName()).thenReturn(studentId);
        when(service.edit(editTaskDto, studentId)).thenReturn(briefTaskDto);

        BriefTaskDto dto = controller.editTask(editTaskDto, principal);

        assertThat(dto, is(briefTaskDto));
        verify(service).edit(editTaskDto, studentId);
    }

    @Test
    public void getTask() throws Exception {
        String studentId = "X4ee";
        long taskId = 904L;
        DetailedTaskDto detailedTaskDto = new DetailedTaskDto();
        when(principal.getName()).thenReturn(studentId);
        when(service.get(taskId, studentId)).thenReturn(detailedTaskDto);

        DetailedTaskDto dto = controller.getTask(taskId, principal);

        assertThat(dto, is(detailedTaskDto));
        verify(service).get(taskId, studentId);
    }

    @Test(expected = TaskNotFoundException.class)
    public void getTaskException() throws Exception {
        String studentId = "NfWSe";
        long taskId = 118L;
        when(principal.getName()).thenReturn(studentId);
        when(service.get(taskId, studentId)).thenThrow(TaskNotFoundException.class);

        controller.getTask(taskId, principal);
    }

    @Test
    public void getAllTasks() {
        long courseId = 740L;
        Set<DetailedTaskDto> detailedTaskDtoSet = Set.of(new DetailedTaskDto());
        when(service.getAll(courseId)).thenReturn(detailedTaskDtoSet);

        Set<DetailedTaskDto> dtos = controller.getAllTasks(courseId);

        assertThat(dtos, is(detailedTaskDtoSet));
        verify(service).getAll(courseId);
    }

    @Test
    public void changeTasksDoneStatus() throws Exception {
        long taskId = 449L;
        doNothing().when(service).changeDoneStatus(taskId);

        controller.changeTasksDoneStatus(taskId);

        verify(service).changeDoneStatus(taskId);
    }

    @Test(expected = TaskNotFoundException.class)
    public void changeTasksDoneStatusException() throws Exception {
        long taskId = 624L;
        doThrow(TaskNotFoundException.class).when(service).changeDoneStatus(taskId);

        controller.changeTasksDoneStatus(taskId);
    }

    @Test
    public void changeTasksCompletion() throws Exception {
        long taskId = 72L;
        String studentId = "qAwp6M";
        when(principal.getName()).thenReturn(studentId);
        doNothing().when(service).changeCompletion(taskId, studentId);

        controller.changeTasksCompletion(taskId, principal);

        verify(service).changeCompletion(taskId, studentId);
    }

    @Test(expected = TaskNotFoundException.class)
    public void changeTasksCompletionTaskException() throws Exception {
        long taskId = 514L;
        String studentId = "D3a6mUp";
        when(principal.getName()).thenReturn(studentId);
        doThrow(TaskNotFoundException.class).when(service).changeCompletion(taskId, studentId);

        controller.changeTasksCompletion(taskId, principal);
    }

    @Test(expected = StudentIdNotFoundException.class)
    public void changeTasksCompletionStudentException() throws Exception {
        long taskId = 175L;
        String studentId = "Z8IMlf0";
        when(principal.getName()).thenReturn(studentId);
        doThrow(StudentIdNotFoundException.class).when(service).changeCompletion(taskId, studentId);

        controller.changeTasksCompletion(taskId, principal);
    }

    @Test
    public void toggleHelpOnTask() throws Exception {
        long taskId = 997L;
        String studentId = "jRj5p8";
        when(principal.getName()).thenReturn(studentId);
        doNothing().when(service).toggleHelp(taskId, studentId);

        controller.toggleHelpOnTask(taskId, principal);

        verify(service).toggleHelp(taskId, studentId);
    }

    @Test(expected = TaskNotFoundException.class)
    public void toggleHelpOnTaskTaskException() throws Exception {
        long taskId = 162L;
        String studentId = "c7glIG";
        when(principal.getName()).thenReturn(studentId);
        doThrow(TaskNotFoundException.class).when(service).toggleHelp(taskId, studentId);

        controller.toggleHelpOnTask(taskId, principal);
    }

    @Test(expected = StudentIdNotFoundException.class)
    public void toggleHelpOnTaskStudentException() throws Exception {
        long taskId = 175L;
        String studentId = "Z8IMlf0";
        when(principal.getName()).thenReturn(studentId);
        doThrow(StudentIdNotFoundException.class).when(service).toggleHelp(taskId, studentId);

        controller.toggleHelpOnTask(taskId, principal);
    }

    @Test
    public void removeTask() throws Exception {
        long taskId = 685L;
        doNothing().when(service).deleteTask(taskId);

        controller.removeTask(taskId);

        verify(service).deleteTask(taskId);
    }

    @Test(expected = TaskNotFoundException.class)
    public void removeTaskException() throws Exception {
        long taskId = 685L;
        doThrow(TaskNotFoundException.class).when(service).deleteTask(taskId);

        controller.removeTask(taskId);
    }

    @Test
    public void removeTasksByCourseId() {
        long courseId = 961L;
        doNothing().when(service).deleteTasksByCourseId(courseId);

        controller.removeTasksByCourseId(courseId);

        verify(service).deleteTasksByCourseId(courseId);
    }
}