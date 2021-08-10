package hu.me.iit.malus.thesis.task.service.impl;

import hu.me.iit.malus.thesis.task.client.FeedbackClient;
import hu.me.iit.malus.thesis.task.client.FileManagementClient;
import hu.me.iit.malus.thesis.task.client.UserClient;
import hu.me.iit.malus.thesis.task.controller.dto.BriefTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.CreateTaskDto;
import hu.me.iit.malus.thesis.task.model.Task;
import hu.me.iit.malus.thesis.task.repository.TaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    public void edit() {
    }

    @Test
    public void get() {
    }

    @Test
    public void getAll() {
    }

    @Test
    public void changeDoneStatus() {
    }

    @Test
    public void changeCompletion() {
    }

    @Test
    public void toggleHelp() {
    }

    @Test
    public void deleteTask() {
    }

    @Test
    public void deleteTasksByCourseId() {
    }
}