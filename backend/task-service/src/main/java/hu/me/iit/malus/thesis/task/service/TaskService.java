package hu.me.iit.malus.thesis.task.service;

import hu.me.iit.malus.thesis.task.controller.dto.BriefTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.CreateTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.DetailedTaskDto;
import hu.me.iit.malus.thesis.task.controller.dto.EditTaskDto;
import hu.me.iit.malus.thesis.task.service.exception.StudentIdNotFoundException;
import hu.me.iit.malus.thesis.task.service.exception.TaskNotFoundException;

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
     * @param task the new task
     * @return the saved task
     */
    BriefTaskDto edit(EditTaskDto task);

    /**
     * Returns a single Task by its id
     * @param taskId id of the Task
     * @return the corresponding Task
     */
    DetailedTaskDto get(Long taskId);

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
     */
    void changeDoneStatus(Long taskId) throws TaskNotFoundException;

    /**
     * Puts the student id to the completed set, and if it is already there, removes it
     *
     * @param taskId    the task to save the students
     * @param studentId the id to save
     */
    void changeCompletion(Long taskId, String studentId) throws StudentIdNotFoundException, TaskNotFoundException;

    /**
     * Checks out the list of the 'help needed' student ids
     *
     * @param taskId the task id
     * @return the list of student ids, who need help
     */
    Set<String> checkIfHelpNeeded(Long taskId) throws TaskNotFoundException;

    /**
     * Toggles (adds-removes) a student id from a task's 'help needed' list
     *
     * @param taskId    id of the task
     * @param studentId id of the student
     */
    void toggleHelp(Long taskId, String studentId) throws StudentIdNotFoundException, TaskNotFoundException;
}
