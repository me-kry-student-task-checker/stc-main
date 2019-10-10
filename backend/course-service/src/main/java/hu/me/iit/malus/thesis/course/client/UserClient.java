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

    @PostMapping("/api/user/saveStudents")
    void saveStudents(@RequestBody Set<Student> studentsToSave);

    @PostMapping("/api/user/saveTeacher")
    void saveTeacher(@RequestBody Teacher teacher);

    @PostMapping("/api/user/saveTeacher")
    void saveTeachers(@RequestBody Set<Teacher> teachersToSave);

    @GetMapping("/api/user/students")
    Set<Student> getAllStudents();

    @GetMapping("/api/user/teachers")
    Set<Teacher> getAllTeachers();

    @GetMapping("/api/user/student/{email}/")
    Student getStudentByEmail(@PathVariable("email") String studentEmail);

    @GetMapping("/api/user/student/assigned/{courseId}")
    Set<Student> getStudentsByAssignedCourseId(@PathVariable("courseId") Long courseId);

    @GetMapping("/api/user/student/notassigned/{courseId}")
    Set<Student> getStudentsByNotAssignedCourseId(@PathVariable("courseId") Long courseId);

    @GetMapping("/api/user/teacher/{email}/")
    Teacher getTeacherByEmail(@PathVariable("email") String teacherEmail);

    @GetMapping("/api/user/teacher/created/{courseId}")
    Teacher getTeacherByCreatedCourseId(@PathVariable("courseId") Long courseId);

    @GetMapping("/api/user/{email}/")
    User getUserByEmail(@PathVariable("email") String userEmail);

    @GetMapping("/api/user/me")
    User getMe();
}
