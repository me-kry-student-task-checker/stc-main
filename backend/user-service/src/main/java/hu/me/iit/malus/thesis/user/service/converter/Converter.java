package hu.me.iit.malus.thesis.user.service.converter;

import hu.me.iit.malus.thesis.user.controller.dto.ActivityDto;
import hu.me.iit.malus.thesis.user.controller.dto.ActivitySaveDto;
import hu.me.iit.malus.thesis.user.controller.dto.StudentDto;
import hu.me.iit.malus.thesis.user.controller.dto.TeacherDto;
import hu.me.iit.malus.thesis.user.model.Activity;
import hu.me.iit.malus.thesis.user.model.Student;
import hu.me.iit.malus.thesis.user.model.Teacher;

public class Converter {

    private Converter() {
    }

    public static StudentDto createStudentDtoFromStudent(Student student) {
        StudentDto dto = new StudentDto();
        dto.setEmail(student.getEmail());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setRole(student.getRole());
        dto.setAssignedCourseIds(student.getAssignedCourseIds());
        dto.setLastActivity(createActivityDtoFromActivity(student.getLastActivity()));
        return dto;
    }

    public static TeacherDto createTeacherDtoFromTeacher(Teacher teacher) {
        TeacherDto dto = new TeacherDto();
        dto.setEmail(teacher.getEmail());
        dto.setFirstName(teacher.getFirstName());
        dto.setLastName(teacher.getLastName());
        dto.setRole(teacher.getRole());
        dto.setCreatedCourseIds(teacher.getCreatedCourseIds());
        dto.setLastActivity(createActivityDtoFromActivity(teacher.getLastActivity()));
        return dto;
    }

    public static ActivityDto createActivityDtoFromActivity(Activity activity) {
        if (activity != null) {
            ActivityDto dto = new ActivityDto();
            dto.setId(activity.getId());
            dto.setType(activity.getType());
            dto.setParentId(activity.getParentId());
            return dto;
        }
        return null;
    }

    public static Activity createActivityFromActivitySaveDto(ActivitySaveDto dto) {
        Activity activity = new Activity();
        activity.setType(dto.getType());
        activity.setParentId(dto.getParentId());
        return activity;
    }
}
