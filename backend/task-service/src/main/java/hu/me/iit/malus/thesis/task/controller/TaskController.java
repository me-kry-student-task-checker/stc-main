package hu.me.iit.malus.thesis.task.controller;

import hu.me.iit.malus.thesis.task.controller.dto.BriefTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.CreateTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.DetailedTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.EditTaskDto;
import hu.me.iit.malus.thesis.task.service.TaskService;
import hu.me.iit.malus.thesis.task.service.exception.TaskNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.Set;

/**
 * Controller endpoint of this service
 *
 * @author Attila Szőke
 */
@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_Teacher')")
    public @Valid BriefTaskDto createTask(@Valid @RequestBody CreateTaskDto dto) {
        return service.create(dto);
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('ROLE_Teacher')")
    public @Valid BriefTaskDto editTask(@Valid @RequestBody EditTaskDto dto) {
        return service.edit(dto);
    }

    @GetMapping("/get/{taskId}")
    public @Valid DetailedTaskDto getTask(@Min(1) @PathVariable Long taskId) {
        return service.get(taskId);
    }

    @GetMapping("/getAll/{courseId}")
    public Set<@Valid DetailedTaskDto> getAllTasks(@Min(1) @PathVariable Long courseId) {
        return service.getAll(courseId);
    }

    @PostMapping("/setDone/{taskId}")
    @PreAuthorize("hasRole('ROLE_Teacher')")
    public void changeTasksDoneStatus(@Min(1) @PathVariable Long taskId) throws TaskNotFoundException {
        service.changeDoneStatus(taskId);
    }

    @PostMapping("/setComplete/{taskId}")
    @PreAuthorize("hasRole('ROLE_Student')")
    public void changeTasksCompletion(@Min(1) @PathVariable Long taskId, Principal principal) throws TaskNotFoundException {
        service.changeCompletion(taskId, principal.getName());
    }

    @GetMapping("/checkHelps/{taskId}")
    @PreAuthorize("hasRole('ROLE_Teacher')")
    public Set<String> checkIfHelpNeededOnTask(@Min(1) @PathVariable Long taskId) throws TaskNotFoundException {
        return service.checkIfHelpNeeded(taskId);
    }

    @PostMapping("/toggleHelp/{taskId}")
    @PreAuthorize("hasRole('ROLE_Student')")
    public void toggleHelpOnTask(@Min(1) @PathVariable Long taskId, Principal principal) throws TaskNotFoundException {
        service.toggleHelp(taskId, principal.getName());
    }
}
