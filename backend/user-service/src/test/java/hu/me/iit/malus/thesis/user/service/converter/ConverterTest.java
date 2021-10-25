package hu.me.iit.malus.thesis.user.service.converter;

import java.util.List;

import hu.me.iit.malus.thesis.user.controller.dto.AdminDto;
import hu.me.iit.malus.thesis.user.controller.dto.StudentDto;
import hu.me.iit.malus.thesis.user.controller.dto.TeacherDto;
import hu.me.iit.malus.thesis.user.controller.dto.UserDto;
import hu.me.iit.malus.thesis.user.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ConverterTest {

    private Student student;
    private Admin admin;
    private Teacher teacher;

    @Before
    public void setUp() throws Exception {
        this.student = new Student(
            "test@example.com",
            "pw1",
            "fn",
            "ln",
            List.of()
        );
        this.admin = new Admin(
            "admin@example.com",
            "admin",
            "First",
            "Last"
        );
        this.teacher = new Teacher(
            "teacher@example.com",
            "teacher",
            "First",
            "Last",
            List.of()
        );
    }

    @Test
    public void createUserDtoFromUser() {
        // GIVEN
        UserDto userDtoFromStudent = Converter.createUserDtoFromUser(student);
        UserDto userDtoFromTeacher = Converter.createUserDtoFromUser(teacher);
        UserDto userDtoFromAdmin = Converter.createUserDtoFromUser(admin);

        // THEN
        assertThat(student.getEmail(), equalTo(userDtoFromStudent.getEmail()));
        assertThat(student.getRole(), equalTo(userDtoFromStudent.getRole()));
        assertThat(student.getFirstName(), equalTo(userDtoFromStudent.getFirstName()));
        assertThat(student.getLastName(), equalTo(userDtoFromStudent.getLastName()));

        assertThat(teacher.getEmail(), equalTo(userDtoFromTeacher.getEmail()));
        assertThat(teacher.getRole(), equalTo(userDtoFromTeacher.getRole()));
        assertThat(teacher.getFirstName(), equalTo(userDtoFromTeacher.getFirstName()));
        assertThat(teacher.getLastName(), equalTo(userDtoFromTeacher.getLastName()));

        assertThat(admin.getEmail(), equalTo(userDtoFromAdmin.getEmail()));
        assertThat(admin.getRole(), equalTo(userDtoFromAdmin.getRole()));
        assertThat(admin.getFirstName(), equalTo(userDtoFromAdmin.getFirstName()));
        assertThat(admin.getLastName(), equalTo(userDtoFromAdmin.getLastName()));
    }

    @Test
    public void createStudentDtoFromStudent() {
        // GIVEN
        StudentDto studentDto = Converter.createStudentDtoFromStudent(student);

        // THEN
        assertThat(student.getEmail(), equalTo(studentDto.getEmail()));
        assertThat(student.getFirstName(), equalTo(studentDto.getFirstName()));
        assertThat(student.getLastName(), equalTo(studentDto.getLastName()));
    }

    @Test
    public void createAdminDtoFromStudent() {
        // GIVEN
        AdminDto adminDto = Converter.createAdminDtoFromStudent(admin);

        // THEN
        assertThat(admin.getEmail(), equalTo(adminDto.getEmail()));
        assertThat(admin.getRole(), equalTo(adminDto.getRole()));
        assertThat(admin.getFirstName(), equalTo(adminDto.getFirstName()));
        assertThat(admin.getLastName(), equalTo(adminDto.getLastName()));
    }

    @Test
    public void createTeacherDtoFromTeacher() {
        // GIVEN
        TeacherDto teacherDto = Converter.createTeacherDtoFromTeacher(teacher);

        // THEN
        assertThat(teacher.getEmail(), equalTo(teacherDto.getEmail()));
        assertThat(teacher.getFirstName(), equalTo(teacherDto.getFirstName()));
        assertThat(teacher.getLastName(), equalTo(teacherDto.getLastName()));
    }

}
