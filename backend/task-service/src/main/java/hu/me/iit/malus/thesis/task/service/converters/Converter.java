package hu.me.iit.malus.thesis.task.service.converters;

import hu.me.iit.malus.thesis.task.controller.dto.BriefTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.CreateTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.EditTaskDto;
import hu.me.iit.malus.thesis.task.model.Task;

public class Converter {

    private Converter() {
    }

    public static Task taskDtoToTask(CreateTaskDto taskDto) {
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setCourseId(taskDto.getCourseId());
        task.setDescription(taskDto.getDescription());
        return task;
    }

    public static Task taskDtoToTask(EditTaskDto taskDto) {
        Task task = new Task();
        task.setId(taskDto.getId());
        task.setName(taskDto.getName());
        task.setCourseId(taskDto.getCourseId());
        task.setDescription(taskDto.getDescription());
        return task;
    }

    public static BriefTaskDto taskToTaskDto(Task task) {
        BriefTaskDto briefTaskDto = new BriefTaskDto();
        briefTaskDto.setId(task.getId());
        briefTaskDto.setName(task.getName());
        briefTaskDto.setDescription(task.getDescription());
        briefTaskDto.setCreationDate(task.getCreationDate());
        briefTaskDto.setDone(task.isDone());
        briefTaskDto.setCourseId(task.getCourseId());
        return briefTaskDto;
    }
}
