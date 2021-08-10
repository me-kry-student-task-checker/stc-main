package hu.me.iit.malus.thesis.task.service.impl;

import hu.me.iit.malus.thesis.dto.ServiceType;
import hu.me.iit.malus.thesis.dto.Student;
import hu.me.iit.malus.thesis.task.client.FeedbackClient;
import hu.me.iit.malus.thesis.task.client.FileManagementClient;
import hu.me.iit.malus.thesis.task.client.UserClient;
import hu.me.iit.malus.thesis.task.controller.dto.BriefTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.CreateTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.DetailedTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.EditTaskDto;
import hu.me.iit.malus.thesis.task.model.Task;
import hu.me.iit.malus.thesis.task.repository.TaskRepository;
import hu.me.iit.malus.thesis.task.service.TaskService;
import hu.me.iit.malus.thesis.task.service.converters.DtoConverter;
import hu.me.iit.malus.thesis.task.service.exception.ForbiddenTaskEditException;
import hu.me.iit.malus.thesis.task.service.exception.StudentIdNotFoundException;
import hu.me.iit.malus.thesis.task.service.exception.TaskNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        Task newTask = DtoConverter.createTaskFromTaskDto(dto);
        newTask.setCreationDate(new Date());
        Task savedTask = repository.save(newTask);
        log.debug("Task created: {}", dto);
        return DtoConverter.createBriefTaskDtoFromTask(savedTask);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BriefTaskDto edit(EditTaskDto dto, String editorsEmail) throws TaskNotFoundException, ForbiddenTaskEditException {
        Task task = repository.findById(dto.getId()).orElseThrow(TaskNotFoundException::new);
        if (!userClient.isRelated(task.getCourseId())) {
            log.warn("Creator of this course {} is not the task editor: {}!", task, editorsEmail);
            throw new ForbiddenTaskEditException();
        }
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        log.debug("Task edited: {}", task);
        return DtoConverter.createBriefTaskDtoFromTask(repository.save(task));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DetailedTaskDto get(Long taskId, String userEmail) throws TaskNotFoundException {
        Task task = repository.findById(taskId).orElseThrow(TaskNotFoundException::new);
        if (!userClient.isRelated(task.getCourseId())) {
            log.warn("This user ({}) is not related to this task's ({}) course!", userEmail, task);
            throw new TaskNotFoundException();
        }
        log.debug("Task queried by id: {}!", taskId);
        return getDetailedTask(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<DetailedTaskDto> getAll(Long courseId) {
        Set<DetailedTaskDto> taskDtoList = repository.findAllByCourseId(courseId).stream().map(this::getDetailedTask).collect(Collectors.toSet());
        log.debug("Tasks queried for course: ({}), found ({}) tasks!", courseId, taskDtoList.size());
        return taskDtoList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeDoneStatus(Long taskId) throws TaskNotFoundException {
        Task task = repository.findById(taskId).orElseThrow(TaskNotFoundException::new);
        task.setDone(!task.isDone());
        repository.save(task);
        log.debug("Task's ({}) done status changed to: {}", task, !task.isDone());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeCompletion(Long taskId, String studentEmail) throws TaskNotFoundException, StudentIdNotFoundException {
        Task task = repository.findById(taskId).orElseThrow(TaskNotFoundException::new);
        Student student = userClient.getStudentByEmail(studentEmail);
        if (!student.getAssignedCourseIds().contains(task.getCourseId())) {
            log.warn("Cannot change task completion, as Student {} is not assigned to this course {}", studentEmail, task.getCourseId());
            throw new StudentIdNotFoundException();
        }
        Set<String> completedStudentIds = task.getCompletedStudentIds();
        if (completedStudentIds.contains(studentEmail)) {
            completedStudentIds.remove(studentEmail);
            task.setCompletedStudentIds(completedStudentIds);
            repository.save(task);
            log.debug("From a task's ({}) completion list this student was removed: {}", task, studentEmail);
            return;
        }
        task.addStudentIdToCompleted(studentEmail);
        repository.save(task);
        log.debug("To task's ({}) completion list this student was added: {}", task, studentEmail);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toggleHelp(Long taskId, String studentEmail) throws StudentIdNotFoundException, TaskNotFoundException {
        Task task = repository.findById(taskId).orElseThrow(TaskNotFoundException::new);
        Student student = userClient.getStudentByEmail(studentEmail);
        if (!student.getAssignedCourseIds().contains(task.getCourseId())) {
            log.warn("Cannot change task completion, as Student {} is not assigned to this course {}", studentEmail, task.getCourseId());
            throw new StudentIdNotFoundException();
        }
        if (task.getHelpNeededStudentIds().contains(studentEmail)) {
            task.getHelpNeededStudentIds().remove(studentEmail);
            repository.save(task);
            log.debug("Removed an id ({}) from the help needed list of task ({})", studentEmail, taskId);
        } else {
            task.addStudentIdToHelp(studentEmail);
            repository.save(task);
            log.debug("Added Student ({}) to the help needed list of task ({})", studentEmail, taskId);
        }
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId) throws TaskNotFoundException {
        Task task = repository.findById(taskId).orElseThrow(TaskNotFoundException::new);
        repository.delete(task);
        removeCommentsAndFiles(task.getId());
    }

    @Override
    @Transactional
    public void deleteTasksByCourseId(Long courseId) {
        List<Task> tasks = repository.deleteByCourseId(courseId);
        tasks.forEach(task -> removeCommentsAndFiles(task.getId()));
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
        taskDto.setFiles(fileManagementClient.getAllFilesByTagId(ServiceType.TASK, task.getId()));

        return taskDto;
    }

    private void removeCommentsAndFiles(Long taskId) {
        feedbackClient.removeTaskCommentsByTaskId(taskId);
        fileManagementClient.removeFilesByServiceAndTagId(ServiceType.TASK, taskId);
    }
}
