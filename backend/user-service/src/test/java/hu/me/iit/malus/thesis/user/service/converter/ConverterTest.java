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
        UserDto userDto = Converter.createUserDtoFromUser(student);

        // THEN
        assertThat(student.getEmail(), equalTo(userDto.getEmail()));
        assertThat(student.getRole(), equalTo(userDto.getRole()));
        assertThat(student.getFirstName(), equalTo(userDto.getFirstName()));
        assertThat(student.getLastName(), equalTo(userDto.getLastName()));
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
