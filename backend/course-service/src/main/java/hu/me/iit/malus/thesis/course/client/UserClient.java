package hu.me.iit.malus.thesis.course.client;

import hu.me.iit.malus.thesis.course.client.dto.Student;
import hu.me.iit.malus.thesis.course.client.dto.Teacher;
import org.apache.commons.lang.RandomStringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Mocked Feign client class for User service
 * @author Attila Szőke
 */
public class UserClient {

    //FIXME when user service is ready, replace this with a Feign interface
    private static Set<Student> students = new HashSet<>();
    private static Set<Teacher> teachers = new HashSet<>();

    private static void init() {

    }

    public static void saveStudent(Student student) {
        students.add(student);
    }

    public static void saveStudents(Set<Student> studentsToAdd) {
        students.addAll(studentsToAdd);
    }

    public static void saveTeacher(Teacher teacher) {
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

    public static Student getStudentByEmail(String studentEmail) {
        init();
        for (Student student : students) {
            if (student.getEmail().equals(studentEmail)) {
                return student;
            }
        }
        return new Student();
    }

    public static Teacher getTeacherByEmail(String teacherEmail) {
        init();
        for (Teacher teacher : teachers) {
            if (teacher.getEmail().equals(teacherEmail)) {
                return teacher;
            }
        }
        return new Teacher();
    }
}
