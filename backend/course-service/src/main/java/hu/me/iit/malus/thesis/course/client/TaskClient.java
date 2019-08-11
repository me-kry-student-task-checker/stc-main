package hu.me.iit.malus.thesis.course.client;

import hu.me.iit.malus.thesis.course.client.dto.CourseComment;
import hu.me.iit.malus.thesis.course.client.dto.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Mocked Feign client class for Task service
 * @author Attila Szőke
 */
public class TaskClient {

    private List<Task> tasks = new ArrayList<>();

    {
        tasks.add(new Task("irj meg mindent", 1L));
    }

    public void save(Task task){
        tasks.add(task);
    }

    public List<Task> getAll(){
        return tasks;
    }

    public Task getByCourseId(Long courseId){
        for (Task task: tasks) {
            if (task.getCourseId() == courseId){
                return task;
            }
        }
        return new Task();
    }
}
