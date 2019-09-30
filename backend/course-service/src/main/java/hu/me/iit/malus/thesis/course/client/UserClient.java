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

    @PostMapping("/saveStudent")
    void saveStudent(@RequestBody Student student);

    @PostMapping("/saveStudents")
    void saveStudents(@RequestBody Set<Student> studentsToSave);

    @PostMapping("/saveTeacher")
    void saveTeacher(@RequestBody Teacher teacher);

    @PostMapping("/saveTeacher")
    void saveTeachers(@RequestBody Set<Teacher> teachersToSave);

    @GetMapping("/students")
    Set<Student> getAllStudents();

    @GetMapping("/teachers")
    Set<Teacher> getAllTeachers();

    @GetMapping("/student/{email:.+}")
    Student getStudentByEmail(@PathVariable("email") String studentEmail);

    @GetMapping("/teacher/{email:.+}")
    Teacher getTeacherByEmail(@PathVariable("email") String teacherEmail);

    @GetMapping("/{email:.+}")
    User getUserByEmail(@PathVariable("email") String userEmail);

    @GetMapping("/me")
    public User getMe();
}
