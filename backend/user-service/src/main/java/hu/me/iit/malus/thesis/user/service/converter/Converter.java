package hu.me.iit.malus.thesis.user.service.converter;

import hu.me.iit.malus.thesis.user.controller.dto.AdminDto;
import hu.me.iit.malus.thesis.user.controller.dto.StudentDto;
import hu.me.iit.malus.thesis.user.controller.dto.TeacherDto;
import hu.me.iit.malus.thesis.user.model.Admin;
import hu.me.iit.malus.thesis.user.model.Student;
import hu.me.iit.malus.thesis.user.model.Teacher;

public class Converter {

    public static StudentDto createStudentDtoFromStudent(Student student) {
        var dto = new StudentDto();
        dto.setEmail(student.getEmail());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setRole(student.getRole());
        dto.setAssignedCourseIds(student.getAssignedCourseIds());
        return dto;
    }

    public static AdminDto createAdminDtoFromStudent(Admin admin) {
        var dto = new AdminDto();
        dto.setEmail(admin.getEmail());
        dto.setFirstName(admin.getFirstName());
        dto.setLastName(admin.getLastName());
        dto.setRole(admin.getRole());
        return dto;
    }

    public static TeacherDto createTeacherDtoFromTeacher(Teacher teacher) {
        var dto = new TeacherDto();
        dto.setEmail(teacher.getEmail());
        dto.setFirstName(teacher.getFirstName());
        dto.setLastName(teacher.getLastName());
        dto.setRole(teacher.getRole());
        dto.setCreatedCourseIds(teacher.getCreatedCourseIds());
        return dto;
    }
}
