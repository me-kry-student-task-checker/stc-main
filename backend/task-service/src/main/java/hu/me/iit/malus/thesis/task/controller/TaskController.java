package hu.me.iit.malus.thesis.task.controller;

import hu.me.iit.malus.thesis.task.controller.converters.Converter;
import hu.me.iit.malus.thesis.task.controller.dto.TaskDto;
import hu.me.iit.malus.thesis.task.model.Task;
import hu.me.iit.malus.thesis.task.service.TaskService;
import hu.me.iit.malus.thesis.task.service.exception.StudentIdNotFoundException;
import hu.me.iit.malus.thesis.task.service.exception.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getAll/{courseId}")
    public Set<Task> getAllTasks(@PathVariable Long courseId) {
        return service.getAll(courseId);
    }

    @PostMapping("/setDone/{taskId}")
    @PreAuthorize("hasRole('ROLE_Teacher')")
    public void changeTasksDoneStatus(@PathVariable Long taskId) throws TaskNotFoundException {
        service.changeDoneStatus(taskId);
    }

    @PostMapping("/setComplete/{taskId}/{studentId}")
    public void changeTasksCompletion(@PathVariable Long taskId, @PathVariable String studentId) throws TaskNotFoundException {
        service.changeCompletion(taskId, studentId);
    }

    @GetMapping("/checkHelps/{taskId}")
    public Set<String> checkIfHelpNeededOnTask(@PathVariable Long taskId) throws TaskNotFoundException {
        return service.checkIfHelpNeeded(taskId);
    }

    @PostMapping("/requestHelp/{taskId}/{studentId}")
    public void requestHelpOnTask(@PathVariable Long taskId, @PathVariable String studentId) throws TaskNotFoundException {
        service.requestHelp(taskId, studentId);
    }

    @PostMapping("/resolveHelp/{taskId}/{studentId}")
    public void resolveHelpOnTask(@PathVariable Long taskId, @PathVariable String studentId) throws StudentIdNotFoundException, TaskNotFoundException {
        service.resolveHelp(taskId, studentId);
    }
}
