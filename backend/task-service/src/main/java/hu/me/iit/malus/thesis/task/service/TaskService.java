package hu.me.iit.malus.thesis.task.service;

import hu.me.iit.malus.thesis.task.controller.dto.BriefTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.CreateTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.DetailedTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.EditTaskDto;
import hu.me.iit.malus.thesis.task.service.exception.ForbiddenTaskEditException;
import hu.me.iit.malus.thesis.task.service.exception.StudentIdNotFoundException;
import hu.me.iit.malus.thesis.task.service.exception.TaskDeleteRollbackException;
import hu.me.iit.malus.thesis.task.service.exception.TaskNotFoundException;

import java.util.List;
import java.util.Set;

/**
 * Interface of the Task Service
 * Defines all the possible operations for the service
 *
 * @author Attila Szőke
 */
public interface TaskService {

    /**
     * Adds a new task to the database or updates an existing one
     *
     * @param task the new task
     * @return the saved task
     */
    BriefTaskDto create(CreateTaskDto task);

    /**
     * Adds a new task to the database or updates an existing one
     *
     * @param task         the new task
     * @param editorsEmail the editors email
     * @return the saved task
     * @throws TaskNotFoundException      the task not found exception
     * @throws ForbiddenTaskEditException the forbidden task edit exception
     */
    BriefTaskDto edit(EditTaskDto task, String editorsEmail) throws TaskNotFoundException, ForbiddenTaskEditException;

    /**
     * Returns a single Task by its id
     *
     * @param taskId    id of the Task
     * @param userEmail the user email
     * @return the corresponding Task
     * @throws TaskNotFoundException the task not found exception
     */
    DetailedTaskDto get(Long taskId, String userEmail) throws TaskNotFoundException;

    /**
     * Gets every task based on it's course id
     *
     * @param courseId the id of the course to get all tasks from
     * @return the list of tasks
     */
    Set<DetailedTaskDto> getAll(Long courseId);

    /**
     * Negates the done flag of a task
     *
     * @param taskId the id of the task
     * @throws TaskNotFoundException the task not found exception
     */
    void changeDoneStatus(Long taskId) throws TaskNotFoundException;

    /**
     * Puts the student id to the completed set, and if it is already there, removes it
     *
     * @param taskId    the task to save the students
     * @param studentId the id to save
     * @throws StudentIdNotFoundException the student id not found exception
     * @throws TaskNotFoundException      the task not found exception
     */
    void toggleCompletion(Long taskId, String studentId) throws StudentIdNotFoundException, TaskNotFoundException;

    /**
     * Toggles (adds-removes) a student id from a task's 'help needed' list
     *
     * @param taskId    id of the task
     * @param studentId id of the student
     * @throws StudentIdNotFoundException the student id not found exception
     * @throws TaskNotFoundException      the task not found exception
     */
    void toggleHelp(Long taskId, String studentId) throws StudentIdNotFoundException, TaskNotFoundException;


    /**
     * Deletes a task.
     *
     * @param taskId the task id
     * @throws TaskNotFoundException       the task not found exception
     * @throws TaskDeleteRollbackException the task delete rollback exception
     */
    void deleteTask(Long taskId) throws TaskNotFoundException, TaskDeleteRollbackException;

    /**
     * 2PC prepare phase, prepare removal of task by courseId.
     *
     * @param courseId the course id
     * @return the transaction key
     */
    String prepareRemoveTaskByTaskIds(List<Long> courseId);

    /**
     * 2PC commit phase, commit removal of task by courseId.
     *
     * @param transactionKey the transaction key
     */
    void commitRemoveTaskByCourseId(String transactionKey);

    /**
     * 2PC rollback phase, rollback removal of task by courseId.
     *
     * @param transactionKey the transaction key
     */
    void rollbackRemoveTaskByCourseId(String transactionKey);
}
