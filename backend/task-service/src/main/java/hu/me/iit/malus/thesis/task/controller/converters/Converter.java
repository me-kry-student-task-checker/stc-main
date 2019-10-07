package hu.me.iit.malus.thesis.task.controller.converters;

import hu.me.iit.malus.thesis.task.controller.dto.TaskDto;
import hu.me.iit.malus.thesis.task.model.Task;

public class Converter {

    public static Task taskDtoToTask(TaskDto taskDto) {
        Task task = new Task();
        task.setId(taskDto.getId());
        task.setName(taskDto.getName());
        task.setCourseId(taskDto.getCourseId());
        task.setDescription(taskDto.getDescription());
        return task;
    }
}
