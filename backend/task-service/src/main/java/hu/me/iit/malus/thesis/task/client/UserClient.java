package hu.me.iit.malus.thesis.task.client;

import hu.me.iit.malus.thesis.task.client.dto.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

/**
 * Feign client class for the User service
 * @author Javorek Dénes
 */
@FeignClient(name = "user-service")
public interface UserClient {

    @PostMapping("/api/user/saveStudent")
    void saveStudent(@RequestBody Student student);

    @PostMapping("/api/user/saveStudents")
    void saveStudents(@RequestBody Set<Student> studentsToSave);

    @GetMapping("/api/user/student/{email}/")
    Student getStudentByEmail(@PathVariable("email") String studentEmail);

    @GetMapping("/api/user/student/assigned/{courseId}")
    Set<Student> getStudentsByAssignedCourseId(@PathVariable("courseId") Long courseId);

    @GetMapping("/api/user/student/notassigned/{courseId}")
    Set<Student> getStudentsByNotAssignedCourseId(@PathVariable("courseId") Long courseId);
}
