package hu.me.iit.malus.thesis.course.client;

import hu.me.iit.malus.thesis.course.client.dto.Student;
import hu.me.iit.malus.thesis.course.client.dto.Teacher;
import hu.me.iit.malus.thesis.course.client.dto.User;
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

    @PostMapping("/api/user/saveCourseCreation")
    void saveCourseCreation(@RequestBody Long courseId);

    @GetMapping("/api/user/student/{email}/")
    Student getStudentByEmail(@PathVariable("email") String studentEmail);

    @GetMapping("/api/user/teacher/{email}/")
    Teacher getTeacherByEmail(@PathVariable("email") String teacherEmail);

    @GetMapping("/api/user/student/assigned/{courseId}")
    Set<Student> getStudentsByAssignedCourseId(@PathVariable("courseId") Long courseId);

    @GetMapping("/api/user/isRelated/course/{courseId}")
    Boolean isRelated(@PathVariable("courseId") Long courseId);

    @GetMapping("/api/user/related/course")
    Set<Long> getRelatedCourseIds();

    @GetMapping("/api/user/teacher/created/{courseId}")
    Teacher getTeacherByCreatedCourseId(@PathVariable("courseId") Long courseId);

    @GetMapping("/api/user/me")
    User getMe();
}
