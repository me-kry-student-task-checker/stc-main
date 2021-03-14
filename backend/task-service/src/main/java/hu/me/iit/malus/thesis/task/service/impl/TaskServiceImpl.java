package hu.me.iit.malus.thesis.task.service.impl;

import hu.me.iit.malus.thesis.task.client.FeedbackClient;
import hu.me.iit.malus.thesis.task.client.FileManagementClient;
import hu.me.iit.malus.thesis.task.client.UserClient;
import hu.me.iit.malus.thesis.task.client.dto.Student;
import hu.me.iit.malus.thesis.task.controller.dto.BriefTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.CreateTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.DetailedTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.EditTaskDto;
import hu.me.iit.malus.thesis.task.model.Task;
import hu.me.iit.malus.thesis.task.repository.TaskRepository;
import hu.me.iit.malus.thesis.task.service.TaskService;
import hu.me.iit.malus.thesis.task.service.converters.Converter;
import hu.me.iit.malus.thesis.task.service.exception.StudentIdNotFoundException;
import hu.me.iit.malus.thesis.task.service.exception.TaskNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;
    private final FeedbackClient feedbackClient;
    private final FileManagementClient fileManagementClient;
    private final UserClient userClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public BriefTaskDto create(CreateTaskDto dto) {
        Task newTask = Converter.createTaskFromTaskDto(dto);
        newTask.setCreationDate(new Date());
        Task savedTask = repository.save(newTask);
        log.debug("Task created: {}", dto);
        return Converter.createBriefTaskDtoFromTask(savedTask);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BriefTaskDto edit(EditTaskDto dto) {
        Task taskToChange = repository.getOne(dto.getId());
        taskToChange.setName(dto.getName());
        taskToChange.setDescription(dto.getDescription());
        Task editedTask = repository.save(taskToChange);
        log.debug("Task edited: {}", editedTask);
        return Converter.createBriefTaskDtoFromTask(editedTask);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DetailedTaskDto get(Long taskId) {
        Optional<Task> optTask = repository.findById(taskId);
        if (optTask.isPresent()) {
            DetailedTaskDto taskDto = getDetailedTask(optTask.get());
            log.debug("Task queried by id: {}, successfully", taskId);
            return taskDto;
        } else {
            log.warn("No task found with this id: {}", taskId);
            throw new TaskNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<DetailedTaskDto> getAll(Long courseId) {
        Optional<Set<Task>> optTasks = repository.findAllByCourseId(courseId);
        if (optTasks.isPresent()) {
            Set<DetailedTaskDto> taskDtos = new HashSet<>();
            Set<Task> tasks = optTasks.get();
            for (Task task : tasks) {
                taskDtos.add(getDetailedTask(task));
            }
            log.debug("Tasks queried for course: {}, found {} tasks", courseId, taskDtos.size());
            return taskDtos;
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
            log.debug("Task's ({}) done status changed to: {}", task, !task.isDone());
        } else {
            log.warn("No task found with this task id: {}", taskId);
            throw new TaskNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeCompletion(Long taskId, String studentId) throws StudentIdNotFoundException, TaskNotFoundException {
        Optional<Task> opt = repository.findById(taskId);
        if (opt.isPresent()) {
            Task task = opt.get();

            Student student = userClient.getStudentByEmail(studentId);
            if (!student.getAssignedCourseIds().contains(task.getCourseId())) {
                log.warn("Cannot change task completion, as Student {} is not assigned to this course {}",
                        studentId, task.getCourseId());
                throw new StudentIdNotFoundException();
            }

            Set<String> completedStudentIds = task.getCompletedStudentIds();
            if (completedStudentIds.contains(studentId)) {
                completedStudentIds.remove(studentId);
                task.setCompletedStudentIds(completedStudentIds);
                repository.save(task);
                log.debug("From a task's ({}) completion list this student was removed: {}", task, studentId);
                return;
            }
            task.addStudentIdToCompleted(studentId);
            repository.save(task);
            log.debug("To task's ({}) completion list this student was added: {}", task, studentId);
        } else {
            log.warn("No task found with this task id: {}", taskId);
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
            log.debug("Task's ({}) help needed list returned: {}", task, helpNeededList);
            return helpNeededList;
        } else {
            log.warn("No task found with this task id: {}", taskId);
            throw new TaskNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toggleHelp(Long taskId, String studentId) throws StudentIdNotFoundException {
        Optional<Task> optTask = repository.findById(taskId);
        if (optTask.isPresent()) {
            Task task = optTask.get();

            Student student = userClient.getStudentByEmail(studentId);
            if (!student.getAssignedCourseIds().contains(task.getCourseId())) {
                log.warn("Cannot change task completion, as Student {} is not assigned to this course {}",
                        studentId, task.getCourseId());
                throw new StudentIdNotFoundException();
            }

            if (task.getHelpNeededStudentIds().contains(studentId)) {
                task.getHelpNeededStudentIds().remove(studentId);
                repository.save(task);
                log.debug("Removed an id ({}) from the help needed list of task ({})", studentId, taskId);
            } else {
                task.addStudentIdToHelp(studentId);
                repository.save(task);
                log.debug("Added Student ({}) to the help needed list of task ({})", studentId, taskId);
            }
        } else {
            log.warn("Could not toggle help request for {} - No task found with id: {}", studentId, taskId);
            throw new TaskNotFoundException();
        }
    }

    private DetailedTaskDto getDetailedTask(Task task) {
        DetailedTaskDto taskDto = new DetailedTaskDto(task);

        Set<Student> allStudentsInCourse = userClient.getStudentsByAssignedCourseId(task.getCourseId());
        Set<Student> completed = new HashSet<>();
        Set<Student> helpNeeded = new HashSet<>();
        for (Student student : allStudentsInCourse) {
            if (task.getCompletedStudentIds().contains(student.getEmail()))
                completed.add(student);
            if (task.getHelpNeededStudentIds().contains(student.getEmail()))
                helpNeeded.add(student);
        }
        taskDto.setCompletedStudents(completed);
        taskDto.setHelpNeededStudents(helpNeeded);
        taskDto.setComments(feedbackClient.getAllTaskComments(task.getId()));
        taskDto.setFiles(fileManagementClient.getAllFilesByTagId(hu.me.iit.malus.thesis.task.client.dto.Service.TASK, task.getId()).getBody());

        return taskDto;
    }
}
