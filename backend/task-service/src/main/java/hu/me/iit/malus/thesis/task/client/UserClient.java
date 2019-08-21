package hu.me.iit.malus.thesis.task.client;

import hu.me.iit.malus.thesis.task.client.dto.Student;
import hu.me.iit.malus.thesis.task.client.dto.Teacher;
import org.apache.commons.lang.RandomStringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Mocked Feign client class for User service
 *
 * @author Attila Szőke
 */
public class UserClient {

    //FIXME when user service is ready, replace this with a Feign interface
    private static Set<Student> students = new HashSet<>();
    private static Set<Teacher> teachers = new HashSet<>();

    private static void init() {
        teachers.add(new Teacher("lala@lali.com", RandomStringUtils.randomAlphabetic(5),
                RandomStringUtils.randomAlphabetic(5), new ArrayList<>(Arrays.asList(9L, 1L))));
        teachers.add(new Teacher("a@b.com", RandomStringUtils.randomAlphabetic(5),
                RandomStringUtils.randomAlphabetic(5), new ArrayList<>(Arrays.asList(3L))));
        students.add(new Student(
                "adsdasda@mail.com",
                RandomStringUtils.randomAlphabetic(5),
                RandomStringUtils.randomAlphabetic(5),
                new ArrayList<>(Arrays.asList(9L, 1L, 3L))));
        students.add(new Student(
                "putty@mail.com",
                RandomStringUtils.randomAlphabetic(5),
                RandomStringUtils.randomAlphabetic(5),
                new ArrayList<>()));

    }

    public static void save(Student student) {
        students.add(student);
    }

    public static void save(Set<Student> studentsToAdd) {
        students.addAll(studentsToAdd);
    }

    public static void save(Teacher teacher) {
        teachers.add(teacher);
    }

    public static Set<Student> getAllStudents() {
        init();
        return students;
    }

    public static Set<Teacher> getAllTeachers() {
        init();
        return teachers;
    }

    public static Student getStudentById(String studentId) {
        init();
        for (Student student : students) {
            if (student.getId().equals(studentId)) {
                return student;
            }
        }
        return new Student();
    }

    public static Teacher getTeacherById(String teacherId) {
        init();
        for (Teacher teacher : teachers) {
            if (teacher.getId().equals(teacherId)) {
                return teacher;
            }
        }
        return new Teacher();
    }
}
