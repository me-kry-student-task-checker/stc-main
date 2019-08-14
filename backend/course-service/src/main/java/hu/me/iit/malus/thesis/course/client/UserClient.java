package hu.me.iit.malus.thesis.course.client;

import hu.me.iit.malus.thesis.course.client.dto.Student;
import hu.me.iit.malus.thesis.course.client.dto.Teacher;
import org.apache.commons.lang.RandomStringUtils;

import java.util.*;

/**
 * Mocked Feign client class for User service
 * @author Attila Szőke
 */
public class UserClient {

    //FIXME when user service is ready, replace this with a Feign interface
    private static Set<Student> students = new HashSet<>();
    private static Set<Teacher> teachers = new HashSet<>();

    {
        teachers.add(new Teacher("lala@lali.com", RandomStringUtils.randomAlphabetic(5),
                RandomStringUtils.randomAlphabetic(5), new ArrayList<>(Arrays.asList(1L, 2L))));
        teachers.add(new Teacher("a@b.com", RandomStringUtils.randomAlphabetic(5),
                RandomStringUtils.randomAlphabetic(5), new ArrayList<>(Collections.singletonList(3L))));
        for (int i = 0; i < 5; i++) {
            students.add(new Student(
                    RandomStringUtils.randomAlphabetic(5) + "@mail.com",
                    RandomStringUtils.randomAlphabetic(5),
                    RandomStringUtils.randomAlphabetic(5),
                    new ArrayList<>(Arrays.asList(1L, 2L, 3L))));
        }
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
        return students;
    }

    public static Set<Teacher> getAllTeachers() {
        return teachers;
    }

    public static Student getStudentById(String studentId) {
        for (Student student : students) {
            if (student.getId().equals(studentId)) {
                return student;
            }
        }
        return new Student();
    }

    public static Teacher getTeacherById(String teacherId) {
        for (Teacher teacher : teachers) {
            if (teacher.getId().equals(teacherId)) {
                return teacher;
            }
        }
        return new Teacher();
    }
}
