package hu.me.iit.malus.thesis.task.controller;

import hu.me.iit.malus.thesis.task.controller.converters.Converter;
import hu.me.iit.malus.thesis.task.controller.dto.DetailedTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.TaskDto;
import hu.me.iit.malus.thesis.task.model.Task;
import hu.me.iit.malus.thesis.task.service.TaskService;
import hu.me.iit.malus.thesis.task.service.exception.StudentIdNotFoundException;
import hu.me.iit.malus.thesis.task.service.exception.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

/**
 * Controller endpoint of this service
 *
 * @author Attila Szőke
 */
@RestController
@RequestMapping("/api/task")
public class TaskController {

    private TaskService service;

    @Autowired
    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_Teacher')")
    public Task createTask(@RequestBody TaskDto task) {
        return service.create(Converter.taskDtoToTask(task));
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('ROLE_Teacher')")
    public Task editTask(@RequestBody TaskDto task) {
        return service.edit(Converter.taskDtoToTask(task));
    }

    @GetMapping("/get/{taskId}")
    public DetailedTaskDto getTask(@PathVariable Long taskId) {
        return service.get(taskId);
    }

    @GetMapping("/getAll/{courseId}")
    public Set<DetailedTaskDto> getAllTasks(@PathVariable Long courseId) {
        return service.getAll(courseId);
    }

    @PostMapping("/setDone/{taskId}")
    @PreAuthorize("hasRole('ROLE_Teacher')")
    public void changeTasksDoneStatus(@PathVariable Long taskId) throws TaskNotFoundException {
        service.changeDoneStatus(taskId);
    }

    @PostMapping("/setComplete/{taskId}")
    @PreAuthorize("hasRole('ROLE_Student')")
    public void changeTasksCompletion(@PathVariable Long taskId, Principal principal) throws TaskNotFoundException {
        service.changeCompletion(taskId, principal.getName());
    }

    @GetMapping("/checkHelps/{taskId}")
    @PreAuthorize("hasRole('ROLE_Teacher')")
    public Set<String> checkIfHelpNeededOnTask(@PathVariable Long taskId) throws TaskNotFoundException {
        return service.checkIfHelpNeeded(taskId);
    }

    @PostMapping("/toggleHelp/{taskId}")
    @PreAuthorize("hasRole('ROLE_Student')")
    public void toggleHelpOnTask(@PathVariable Long taskId, Principal principal) throws TaskNotFoundException {
        service.toggleHelp(taskId, principal.getName());
    }
}
