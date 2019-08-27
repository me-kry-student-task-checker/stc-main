package hu.me.iit.malus.thesis.task.service.impl;

import hu.me.iit.malus.thesis.task.client.FeedbackClient;
import hu.me.iit.malus.thesis.task.model.Task;
import hu.me.iit.malus.thesis.task.repository.TaskRepository;
import hu.me.iit.malus.thesis.task.service.TaskService;
import hu.me.iit.malus.thesis.task.service.exception.StudentIdNotFoundException;
import hu.me.iit.malus.thesis.task.service.exception.TaskNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

/**
 * Default implementation of the Task Service interface
 *
 * @author Attila Szőke
 */
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    /**
     * Instantiates a new TaskServiceImpl class
     */
    @Autowired
    public TaskServiceImpl(TaskRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task create(Task task) {
        task.setDone(false);
        log.info("Task created: {}", task);
        return repository.save(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task edit(Task task) {
        log.info("Task edited: {}", task);
        return repository.save(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Task> getAll(Long courseId) throws TaskNotFoundException {
        Optional<Set<Task>> opt = repository.findAllByCourseId(courseId);
        if (opt.isPresent()) {
            Set<Task> tasks = opt.get();
            for (Task task : tasks) {
                task.setComments(FeedbackClient.getByTaskId(task.getId()));
            }
            log.info("Task queried: {}", tasks);
            return tasks;
        } else {
            log.error("No task found with this course id: {}", courseId);
            throw new TaskNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeDoneStatus(Long taskId) throws TaskNotFoundException {
        Optional<Task> opt = repository.findById(taskId);
        if (opt.isPresent()) {
            Task task = opt.get();
            task.setDone(!task.isDone());
            repository.save(task);
            log.info("Task's ({}) done status changed to: {}", task, !task.isDone());
        } else {
            log.error("No task found with this task id: {}", taskId);
            throw new TaskNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeCompletion(Long taskId, String studentId) throws TaskNotFoundException {
        Optional<Task> opt = repository.findById(taskId);
        if (opt.isPresent()) {
            Task task = opt.get();
            Set<String> completedStudentIds = task.getCompletedStudentIds();
            if (completedStudentIds.contains(studentId)) {
                completedStudentIds.remove(studentId);
                task.setCompletedStudentIds(completedStudentIds);
                repository.save(task);
                log.info("From a task's ({}) completion list this student was removed: {}", task, studentId);
                return;
            }
            task.addStudentIdToCompleted(studentId);
            repository.save(task);
            log.info("To task's ({}) completion list this student was added: {}", task, studentId);
        } else {
            log.error("No task found with this task id: {}", taskId);
            throw new TaskNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> checkIfHelpNeeded(Long taskId) throws TaskNotFoundException {
        Optional<Task> opt = repository.findById(taskId);
        if (opt.isPresent()) {
            Task task = opt.get();
            Set<String> helpNeededList = task.getHelpNeededStudentIds();
            log.info("Task's ({}) help needed list returned: {}", task, helpNeededList);
            return helpNeededList;
        } else {
            log.error("No task found with this task id: {}", taskId);
            throw new TaskNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void requestHelp(Long taskId, String studentId) throws TaskNotFoundException {
        Optional<Task> opt = repository.findById(taskId);
        if (opt.isPresent()) {
            Task task = opt.get();
            task.addStudentIdToHelp(studentId);
            log.info("Added an id ({}) to a task's ({}) help needed list", studentId, task);
        } else {
            log.error("No task found with this task id: {}", taskId);
            throw new TaskNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resolveHelp(Long taskId, String studentId) throws StudentIdNotFoundException, TaskNotFoundException {
        Optional<Task> opt = repository.findById(taskId);
        if (opt.isPresent()) {
            Task task = opt.get();
            if (task.getHelpNeededStudentIds().contains(studentId)) {
                task.getHelpNeededStudentIds().remove(studentId);
                repository.save(task);
                log.info("Removed an id ({}) from a task's ({}) help needed list", studentId, task);
            } else {
                log.error("No student with this id ({}) found in the help needed list of this task: {}", studentId, task);
                throw new StudentIdNotFoundException();
            }
        } else {
            log.error("No task found with this task id: {}", taskId);
            throw new TaskNotFoundException();
        }
    }
}
