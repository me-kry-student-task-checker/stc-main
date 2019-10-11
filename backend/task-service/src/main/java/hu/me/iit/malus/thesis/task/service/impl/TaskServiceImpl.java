package hu.me.iit.malus.thesis.task.service.impl;

import hu.me.iit.malus.thesis.task.client.FeedbackClient;
import hu.me.iit.malus.thesis.task.client.FileManagementClient;
import hu.me.iit.malus.thesis.task.model.Task;
import hu.me.iit.malus.thesis.task.repository.TaskRepository;
import hu.me.iit.malus.thesis.task.service.TaskService;
import hu.me.iit.malus.thesis.task.service.exception.StudentIdNotFoundException;
import hu.me.iit.malus.thesis.task.service.exception.TaskNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
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

    private TaskRepository repository;
    private FeedbackClient feedbackClient;
    private FileManagementClient fileManagementClient;

    /**
     * Instantiates a new TaskServiceImpl class
     */
    @Autowired
    public TaskServiceImpl(TaskRepository repository, FeedbackClient feedbackClient, FileManagementClient fileManagementClient) {
        this.repository = repository;
        this.feedbackClient = feedbackClient;
        this.fileManagementClient = fileManagementClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task create(Task task) {
        task.setDone(false);
        task.setCreationDate(new Date());
        log.info("Task created: {}", task);
        return repository.save(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task edit(Task task) {
        Task taskToChange = repository.getOne(task.getId());
        taskToChange.setName(task.getName());
        taskToChange.setDescription(task.getDescription());
        log.info("Task edited: {}", task);
        return repository.save(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task get(Long taskId) {
        Optional<Task> optTask = repository.findById(taskId);
        if (optTask.isPresent()) {
            Task task = optTask.get();
            task.setComments(feedbackClient.getAllTaskComments(task.getId()));
            task.setFiles(fileManagementClient.getAllFilesByTagId(hu.me.iit.malus.thesis.task.client.dto.Service.TASK, task.getId()).getBody());
            log.info("Task queried: {}", task);
            return task;
        } else {
            log.error("No task found with this id: {}", taskId);
            throw new TaskNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Task> getAll(Long courseId) {
        Optional<Set<Task>> opt = repository.findAllByCourseId(courseId);
        if (opt.isPresent()) {
            Set<Task> tasks = opt.get();
            for (Task task : tasks) {
                task.setComments(feedbackClient.getAllTaskComments(task.getId()));
                task.setFiles(fileManagementClient.getAllFilesByTagId(hu.me.iit.malus.thesis.task.client.dto.Service.TASK, task.getId()).getBody());
            }
            log.info("Tasks queried for course ({}): {}", courseId, tasks);
            return tasks;
        } else {
            log.warn("No task found for this course id: {}", courseId);
            return new HashSet<>();
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
    public void toggleHelp(Long taskId, String studentId) {
        Optional<Task> optTask = repository.findById(taskId);
        if (optTask.isPresent()) {
            Task task = optTask.get();
            if (task.getHelpNeededStudentIds().contains(studentId)) {
                task.getHelpNeededStudentIds().remove(studentId);
                repository.save(task);
                log.info("Removed an id ({}) from the help needed list of task ({})", studentId, taskId);
            } else {
                task.addStudentIdToHelp(studentId);
                repository.save(task);
                log.info("Added Student ({}) to the help needed list of task ({})", studentId, taskId);
            }
        } else {
            log.error("Could not toggle help request for {} - No task found with id: {}", studentId, taskId);
            throw new TaskNotFoundException();
        }
    }
}
