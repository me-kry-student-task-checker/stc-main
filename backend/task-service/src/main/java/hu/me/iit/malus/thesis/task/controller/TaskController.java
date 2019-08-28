package hu.me.iit.malus.thesis.task.controller;

import hu.me.iit.malus.thesis.task.model.Task;
import hu.me.iit.malus.thesis.task.service.TaskService;
import hu.me.iit.malus.thesis.task.service.exception.StudentIdNotFoundException;
import hu.me.iit.malus.thesis.task.service.exception.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Task createTask(@RequestBody Task task) {
        return service.create(task);
    }

    @PostMapping("/edit")
    public Task editTask(@RequestBody Task task) {
        return service.edit(task);
    }

    @GetMapping("/getAll/{courseId}")
    public Set<Task> getAllTasks(@PathVariable Long courseId) {
        return service.getAll(courseId);
    }

    @PostMapping("/setDone/{taskId}")
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
