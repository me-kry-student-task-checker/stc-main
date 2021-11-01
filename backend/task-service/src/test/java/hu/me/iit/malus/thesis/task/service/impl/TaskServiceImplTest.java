package hu.me.iit.malus.thesis.task.service.impl;

import hu.me.iit.malus.thesis.dto.ServiceType;
import hu.me.iit.malus.thesis.dto.Student;
import hu.me.iit.malus.thesis.task.client.FeedbackClient;
import hu.me.iit.malus.thesis.task.client.FileManagementClient;
import hu.me.iit.malus.thesis.task.client.UserClient;
import hu.me.iit.malus.thesis.task.controller.dto.BriefTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.CreateTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.DetailedTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.EditTaskDto;
import hu.me.iit.malus.thesis.task.model.Task;
import hu.me.iit.malus.thesis.task.repository.TaskRepository;
import hu.me.iit.malus.thesis.task.service.exception.ForbiddenTaskEditException;
import hu.me.iit.malus.thesis.task.service.exception.StudentIdNotFoundException;
import hu.me.iit.malus.thesis.task.service.exception.TaskNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository repository;

    @Mock
    private FeedbackClient feedbackClient;

    @Mock
    private FileManagementClient fileManagementClient;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private TaskServiceImpl service;

    @Captor
    private ArgumentCaptor<Task> captor;

    @Test
    public void create() {
        long id = 852L;
        String name = "n7q4ZW";
        String description = "94HH";
        long courseId = 137L;
        CreateTaskDto createTaskDto = new CreateTaskDto();
        createTaskDto.setName(name);
        createTaskDto.setDescription(description);
        createTaskDto.setCourseId(courseId);
        Task task = new Task();
        task.setId(id);
        task.setName(name);
        task.setDescription(description);
        task.setCourseId(courseId);
        when(repository.save(any())).thenReturn(task);

        BriefTaskDto dto = service.create(createTaskDto);

        assertThat(dto.getId(), is(id));
        assertThat(dto.getName(), is(name));
        assertThat(dto.getDescription(), is(description));
        assertThat(dto.getCourseId(), is(courseId));
        verify(repository).save(any());
    }

    @Test
    public void edit() throws Exception {
        String editorsEmail = "5qLc15t";
        long taskId = 774L;
        String editTaskName = "B64tUZA";
        String editTaskDescription = "IaR68pqx";
        EditTaskDto editTaskDto = new EditTaskDto();
        editTaskDto.setId(taskId);
        editTaskDto.setName(editTaskName);
        editTaskDto.setDescription(editTaskDescription);
        String description = "RAOw33qc";
        String name = "Lbe1Xh0";
        long courseId = 276L;
        Task task = new Task();
        task.setId(taskId);
        task.setName(name);
        task.setDescription(description);
        task.setCourseId(courseId);
        when(repository.findByIdAndRemovedFalse(taskId)).thenReturn(Optional.of(task));
        when(userClient.isRelated(courseId)).thenReturn(true);
        when(repository.save(task)).thenReturn(task);

        BriefTaskDto dto = service.edit(editTaskDto, editorsEmail);

        assertThat(dto.getId(), is(taskId));
        assertThat(dto.getName(), is(editTaskName));
        assertThat(dto.getDescription(), is(editTaskDescription));
        verify(repository).findByIdAndRemovedFalse(taskId);
        verify(userClient).isRelated(courseId);
        verify(repository).save(task);
    }

    @Test(expected = TaskNotFoundException.class)
    public void editNotFoundException() throws Exception {
        long taskId = 156L;
        EditTaskDto editTaskDto = new EditTaskDto();
        editTaskDto.setId(taskId);
        when(repository.findByIdAndRemovedFalse(taskId)).thenReturn(Optional.empty());

        service.edit(editTaskDto, "xz5uYR");
    }

    @Test(expected = ForbiddenTaskEditException.class)
    public void editForbiddenException() throws Exception {
        long taskId = 156L;
        EditTaskDto editTaskDto = new EditTaskDto();
        editTaskDto.setId(taskId);
        long courseId = 175L;
        Task task = new Task();
        task.setId(taskId);
        task.setCourseId(courseId);
        when(repository.findByIdAndRemovedFalse(taskId)).thenReturn(Optional.of(task));
        when(userClient.isRelated(courseId)).thenReturn(false);

        service.edit(editTaskDto, "xz5uYR");
    }

    @Test
    public void get() throws Exception {
        long taskId = 905L;
        String name = "OQtk4GfT";
        String description = "GE019SkC";
        Date creationDate = new Date();
        boolean done = true;
        long courseId = 975L;
        Task task = new Task();
        task.setId(taskId);
        task.setName(name);
        task.setDescription(description);
        task.setCreationDate(creationDate);
        task.setDone(done);
        task.setCourseId(courseId);
        when(repository.findByIdAndRemovedFalse(taskId)).thenReturn(Optional.of(task));
        when(userClient.isRelated(courseId)).thenReturn(true);
        when(feedbackClient.getAllTaskComments(taskId)).thenReturn(new ArrayList<>());
        when(fileManagementClient.getAllFilesByTagId(ServiceType.TASK, taskId)).thenReturn(new HashSet<>());

        DetailedTaskDto dto = service.get(taskId, "foIM");

        assertThat(dto.getId(), is(taskId));
        assertThat(dto.getName(), is(name));
        assertThat(dto.getDescription(), is(description));
        assertThat(dto.getCreationDate(), is(creationDate));
        assertThat(dto.isDone(), is(done));
        assertThat(dto.getCourseId(), is(courseId));
        assertThat(dto.getCompletedStudents().size(), is(0));
        assertThat(dto.getHelpNeededStudents().size(), is(0));
        assertThat(dto.getComments().size(), is(0));
        assertThat(dto.getFiles().size(), is(0));
        verify(repository).findByIdAndRemovedFalse(taskId);
        verify(userClient).isRelated(courseId);
        verify(feedbackClient).getAllTaskComments(taskId);
        verify(fileManagementClient).getAllFilesByTagId(ServiceType.TASK, taskId);
    }

    @Test(expected = TaskNotFoundException.class)
    public void getNotFoundException() throws Exception {
        long taskId = 905L;
        when(repository.findByIdAndRemovedFalse(taskId)).thenReturn(Optional.empty());

        service.get(taskId, "foIM");
    }

    @Test(expected = TaskNotFoundException.class)
    public void getNotFoundExceptionBecauseNotRelated() throws Exception {
        long taskId = 905L;
        Task task = new Task();
        task.setId(taskId);
        when(repository.findByIdAndRemovedFalse(taskId)).thenReturn(Optional.of(task));

        service.get(taskId, "foIM");
    }

    @Test
    public void getAll() {
        long taskId = 905L;
        String name = "OQtk4GfT";
        String description = "GE019SkC";
        Date creationDate = new Date();
        boolean done = true;
        long courseId = 975L;
        Task task = new Task();
        task.setId(taskId);
        task.setName(name);
        task.setDescription(description);
        task.setCreationDate(creationDate);
        task.setDone(done);
        task.setCourseId(courseId);
        when(repository.findAllByCourseIdAndRemovedFalse(courseId)).thenReturn(List.of(task));
        when(feedbackClient.getAllTaskComments(taskId)).thenReturn(new ArrayList<>());
        when(fileManagementClient.getAllFilesByTagId(ServiceType.TASK, taskId)).thenReturn(new HashSet<>());

        Set<DetailedTaskDto> dtos = service.getAll(courseId);

        assertThat(dtos.size(), is(1));
        verify(repository).findAllByCourseIdAndRemovedFalse(courseId);
        verify(feedbackClient).getAllTaskComments(taskId);
        verify(fileManagementClient).getAllFilesByTagId(ServiceType.TASK, taskId);
    }

    @Test
    public void changeDoneStatus() throws Exception {
        long taskId = 862L;
        boolean done = true;
        Task task = new Task();
        task.setId(taskId);
        task.setDone(done);
        when(repository.findByIdAndRemovedFalse(taskId)).thenReturn(Optional.of(task));

        service.changeDoneStatus(taskId);

        verify(repository).findByIdAndRemovedFalse(taskId);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().isDone(), is(!done));
    }

    @Test(expected = TaskNotFoundException.class)
    public void changeDoneStatusNotFoundException() throws Exception {
        long taskId = 905L;
        when(repository.findByIdAndRemovedFalse(taskId)).thenReturn(Optional.empty());

        service.changeDoneStatus(taskId);
    }

    @Test
    public void changeCompletion() throws Exception {
        long taskId = 270L;
        long courseId = 70L;
        Set<String> completedStudentIds = new HashSet<>();
        Task task = new Task();
        task.setId(taskId);
        task.setCourseId(courseId);
        task.setCompletedStudentIds(completedStudentIds);
        String studentEmail = "XU6";
        List<Long> assignedCourseIds = new ArrayList<>();
        assignedCourseIds.add(courseId);
        Student student = new Student();
        student.setEmail(studentEmail);
        student.setAssignedCourseIds(assignedCourseIds);
        when(repository.findByIdAndRemovedFalse(taskId)).thenReturn(Optional.of(task));
        when(userClient.getStudentByEmail(studentEmail)).thenReturn(student);

        service.toggleCompletion(taskId, studentEmail);
        assertThat(task.getCompletedStudentIds().size(), is(1));
        service.toggleCompletion(taskId, studentEmail);
        assertThat(task.getCompletedStudentIds().size(), is(0));

        verify(repository, times(2)).save(task);
        verify(repository, times(2)).findByIdAndRemovedFalse(taskId);
        verify(userClient, times(2)).getStudentByEmail(studentEmail);
    }

    @Test(expected = TaskNotFoundException.class)
    public void changeCompletionTaskNotFoundException() throws Exception {
        long taskId = 821L;
        when(repository.findByIdAndRemovedFalse(taskId)).thenReturn(Optional.empty());

        service.toggleCompletion(taskId, "1BWM");
    }

    @Test(expected = StudentIdNotFoundException.class)
    public void changeCompletionStudentIdNotFoundException() throws Exception {
        long taskId = 270L;
        Task task = new Task();
        task.setId(taskId);
        String studentEmail = "XU6";
        List<Long> assignedCourseIds = new ArrayList<>();
        Student student = new Student();
        student.setEmail(studentEmail);
        student.setAssignedCourseIds(assignedCourseIds);
        when(repository.findByIdAndRemovedFalse(taskId)).thenReturn(Optional.of(task));
        when(userClient.getStudentByEmail(studentEmail)).thenReturn(student);

        service.toggleCompletion(taskId, studentEmail);
    }

    @Test
    public void toggleHelp() throws Exception {
        long taskId = 471L;
        long courseId = 670L;
        Set<String> helpNeededStudentIds = new HashSet<>();
        Task task = new Task();
        task.setId(taskId);
        task.setCourseId(courseId);
        task.setHelpNeededStudentIds(helpNeededStudentIds);
        String studentEmail = "XU6";
        List<Long> assignedCourseIds = new ArrayList<>();
        assignedCourseIds.add(courseId);
        Student student = new Student();
        student.setEmail(studentEmail);
        student.setAssignedCourseIds(assignedCourseIds);
        when(repository.findByIdAndRemovedFalse(taskId)).thenReturn(Optional.of(task));
        when(userClient.getStudentByEmail(studentEmail)).thenReturn(student);

        service.toggleHelp(taskId, studentEmail);
        assertThat(task.getHelpNeededStudentIds().size(), is(1));
        service.toggleHelp(taskId, studentEmail);
        assertThat(task.getHelpNeededStudentIds().size(), is(0));

        verify(repository, times(2)).save(task);
        verify(repository, times(2)).findByIdAndRemovedFalse(taskId);
        verify(userClient, times(2)).getStudentByEmail(studentEmail);
    }

    @Test(expected = TaskNotFoundException.class)
    public void toggleHelpTaskNotFoundException() throws Exception {
        long taskId = 164L;
        when(repository.findByIdAndRemovedFalse(taskId)).thenReturn(Optional.empty());

        service.toggleHelp(taskId, "1BWM");
    }

    @Test(expected = StudentIdNotFoundException.class)
    public void toggleHelpStudentIdNotFoundException() throws Exception {
        long taskId = 36L;
        Task task = new Task();
        task.setId(taskId);
        String studentEmail = "XU6";
        List<Long> assignedCourseIds = new ArrayList<>();
        Student student = new Student();
        student.setEmail(studentEmail);
        student.setAssignedCourseIds(assignedCourseIds);
        when(repository.findByIdAndRemovedFalse(taskId)).thenReturn(Optional.of(task));
        when(userClient.getStudentByEmail(studentEmail)).thenReturn(student);

        service.toggleHelp(taskId, studentEmail);
    }

    @Test
    public void deleteTask() throws Exception {
        long taskId = 214L;
        long courseId = 345L;
        Set<String> helpNeededStudentIds = new HashSet<>();
        Task task = new Task();
        task.setId(taskId);
        task.setCourseId(courseId);
        task.setHelpNeededStudentIds(helpNeededStudentIds);
        when(repository.findByIdAndRemovedFalse(taskId)).thenReturn(Optional.of(task));

        service.deleteTask(taskId);

        assertThat(task.isRemoved(), is(true));
        verify(repository).save(task);
        verify(feedbackClient).removeTaskCommentsByTaskId(taskId);
        verify(fileManagementClient).removeFilesByServiceAndTagId(ServiceType.TASK, taskId);
        verify(repository).findByIdAndRemovedFalse(taskId);
    }

    @Test(expected = TaskNotFoundException.class)
    public void deleteTaskTaskNotFoundException() throws Exception {
        long taskId = 194L;
        when(repository.findByIdAndRemovedFalse(taskId)).thenReturn(Optional.empty());

        service.deleteTask(taskId);
    }

}