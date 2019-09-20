package hu.me.iit.malus.thesis.task.service;

import hu.me.iit.malus.thesis.task.model.Task;
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
    Task create(Task task);

    /**
     * Adds a new task to the database or updates an existing one
     *
     * @param task the new task
     * @return the saved task
     */
    Task edit(Task task);

    /**
     * Gets every task based on it's course id
     *
     * @param courseId the id of the course to get all tasks from
     * @return the list of tasks
     */
    Set<Task> getAll(Long courseId);

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
    void changeCompletion(Long taskId, String studentId) throws TaskNotFoundException;

    /**
     * Checks out the list of the 'help needed' student ids
     *
     * @param taskId the task id
     * @return the list of student ids, who need help
     */
    Set<String> checkIfHelpNeeded(Long taskId) throws TaskNotFoundException;

    /**
     * Adds a student id to a task's 'help needed' list
     *
     * @param taskId    id of the task
     * @param studentId id of the student
     */
    void requestHelp(Long taskId, String studentId) throws TaskNotFoundException;

    /**
     * Removes a student id from a task's 'help needed' list
     *
     * @param taskId    id of the task
     * @param studentId id of the student
     */
    void resolveHelp(Long taskId, String studentId) throws StudentIdNotFoundException, TaskNotFoundException;

}
