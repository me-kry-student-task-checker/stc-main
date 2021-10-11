package hu.me.iit.malus.thesis.task.service.converters;

import hu.me.iit.malus.thesis.dto.File;
import hu.me.iit.malus.thesis.dto.Student;
import hu.me.iit.malus.thesis.dto.TaskComment;
import hu.me.iit.malus.thesis.task.controller.dto.BriefTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.CreateTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.DetailedTaskDto;
import hu.me.iit.malus.thesis.task.model.Task;

import java.util.List;
import java.util.Set;

public class DtoConverter {

    private DtoConverter() {
    }

    public static Task createTaskFromTaskDto(CreateTaskDto taskDto) {
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setCourseId(taskDto.getCourseId());
        task.setDescription(taskDto.getDescription());
        return task;
    }

    public static BriefTaskDto createBriefTaskDtoFromTask(Task task) {
        BriefTaskDto briefTaskDto = new BriefTaskDto();
        briefTaskDto.setId(task.getId());
        briefTaskDto.setName(task.getName());
        briefTaskDto.setDescription(task.getDescription());
        briefTaskDto.setCreationDate(task.getCreationDate());
        briefTaskDto.setDone(task.isDone());
        briefTaskDto.setCourseId(task.getCourseId());
        return briefTaskDto;
    }

    public static DetailedTaskDto createDetailedTaskDtoFromTas(
            Task task, Set<File> files, Set<Student> helpNeededStudents, Set<Student> completedStudents, List<TaskComment> comments) {
        var dto = new DetailedTaskDto();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setCreationDate(task.getCreationDate());
        dto.setDone(task.isDone());
        dto.setCourseId(task.getCourseId());
        dto.setFiles(files);
        dto.setHelpNeededStudents(helpNeededStudents);
        dto.setCompletedStudents(completedStudents);
        dto.setComments(comments);
        return dto;
    }
}
