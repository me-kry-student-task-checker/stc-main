package hu.me.iit.malus.thesis.course.client;

import hu.me.iit.malus.thesis.course.client.dto.Task;

import java.util.HashSet;
import java.util.Set;

/**
 * Mocked Feign client class for Task service
 * @author Attila Szőke
 */
public class TaskClient {

    //FIXME when task service is ready, replace this with a Feign interface
    private static Set<Task> tasks = new HashSet<>();

    {
        tasks.add(new Task("irj meg mindent", 1L));
        tasks.add(new Task("irj meg mindent majd", 2L));
        tasks.add(new Task("irj meg mindent most", 3L));
    }

    public static void save(Set<Task> task) {
        tasks.addAll(task);
    }

    public static Set<Task> getAll() {
        return tasks;
    }

    public static Set<Task> getAllByCourseId(Long courseId) {
        Set<Task> filteredTasks = new HashSet<>();
        for (Task task: tasks) {
            if (task.getCourseId().equals(courseId)) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }
}
