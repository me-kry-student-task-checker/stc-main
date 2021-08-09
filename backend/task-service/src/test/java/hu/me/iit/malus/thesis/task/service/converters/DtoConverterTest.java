package hu.me.iit.malus.thesis.task.service.converters;

import hu.me.iit.malus.thesis.task.controller.dto.BriefTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.CreateTaskDto;
import hu.me.iit.malus.thesis.task.model.Task;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DtoConverterTest {

    @Test
    public void createTaskFromTaskDto() {
        CreateTaskDto dto = new CreateTaskDto();
        String name = "9l3Mjh2c";
        String description = "x2wBdGQ";
        long courseId = 417L;
        dto.setName(name);
        dto.setDescription(description);
        dto.setCourseId(courseId);

        Task task = DtoConverter.createTaskFromTaskDto(dto);

        assertThat(task.getName(), is(name));
        assertThat(task.getDescription(), is(description));
        assertThat(task.getCourseId(), is(courseId));
    }

    @Test
    public void createBriefTaskDtoFromTask() {
        String name = "gsPa74";
        String description = "MQWv";
        Date creationDate = new Date();
        long courseId = 575L;
        Task task = new Task(name, description, creationDate, false, courseId, new HashSet<>(), new HashSet<>());
        long id = 634L;
        task.setId(id);

        BriefTaskDto dto = DtoConverter.createBriefTaskDtoFromTask(task);

        assertThat(dto.getId(), is(id));
        assertThat(dto.getName(), is(name));
        assertThat(dto.getDescription(), is(description));
        assertThat(dto.getCreationDate(), is(creationDate));
        assertThat(dto.isDone(), is(false));
        assertThat(dto.getCourseId(), is(courseId));
    }
}