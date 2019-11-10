package hu.me.iit.malus.thesis.task.client;

import hu.me.iit.malus.thesis.task.client.dto.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

/**
 * Feign client class for the User service
 * @author Javorek Dénes
 */
@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/api/user/student/{email}/")
    Student getStudentByEmail(@PathVariable("email") String studentEmail);

    @GetMapping("/api/user/student/assigned/{courseId}")
    Set<Student> getStudentsByAssignedCourseId(@PathVariable("courseId") Long courseId);
}
