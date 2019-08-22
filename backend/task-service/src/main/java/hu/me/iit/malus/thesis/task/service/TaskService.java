package hu.me.iit.malus.thesis.task.service;

import hu.me.iit.malus.thesis.task.model.Task;

import java.util.List;

/**
 * Interface of the Task Service
 * Defines all the possible operations for the service
 *
 * @author Javorek Dénes
 * @author Attila Szőke
 */
public interface TaskService {

    /**
     * Adds a new task to the database or updates an existing one
     *
     * @param task the new task
     * @return the saved task
     */
    Task save(Task task);

    /**
     * Adds a list of tasks to the database or changes existing ones
     *
     * @param tasks the list of tasks
     * @return the saved tasks
     */
    List<Task> save(List<Task> tasks);

    /**
     * Gets every task based on it's course id
     *
     * @param courseId the id of the course to get all tasks from
     * @return the list of tasks
     */
    List<Task> getAll(Long courseId);

    /**
     * Negates the done flag of a task
     *
     * @param taskId the id of the task
     */
    void changeCompletionStatus(Long taskId);

    /**
     * Checks out the list of the 'help needed' student ids
     *
     * @param taskId the task id
     * @return the list of student ids, who need help
     */
    List<Long> checkIfHelpNeeded(Long taskId);

    /**
     * Adds a student id to a task's 'help needed' list
     *
     * @param taskId    id of the task
     * @param studentId id of the student
     */
    void requestHelp(Long taskId, String studentId);

    /**
     * Removes a student id from a task's 'help needed' list
     *
     * @param taskId    id of the task
     * @param studentId id of the student
     */
    void resolveHelp(Long taskId, String studentId);

}
