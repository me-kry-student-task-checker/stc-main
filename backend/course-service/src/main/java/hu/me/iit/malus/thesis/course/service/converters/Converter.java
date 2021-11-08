package hu.me.iit.malus.thesis.course.service.converters;

import hu.me.iit.malus.thesis.course.controller.dto.CourseCreateDto;
import hu.me.iit.malus.thesis.course.controller.dto.CourseFullDetailsDto;
import hu.me.iit.malus.thesis.course.controller.dto.CourseOverviewDto;
import hu.me.iit.malus.thesis.course.model.Course;

/**
 * Collection of static Dto converter methods.
 *
 * @author Javorek Dénes
 */
public class Converter {

    public static Course createCourseFromCourseCreateDto(CourseCreateDto dto) {
        Course course = new Course();
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        return course;
    }

    public static CourseOverviewDto createCourseOverviewDtoFromCourse(Course course) {
        CourseOverviewDto courseOverview = new CourseOverviewDto();
        courseOverview.setId(course.getId());
        courseOverview.setName(course.getName());
        courseOverview.setDescription(course.getDescription());
        courseOverview.setCreationDate(course.getCreationDate());
        courseOverview.setCreator(course.getCreator());
        return courseOverview;
    }

    public static CourseFullDetailsDto createCourseFullDetailsDtoFromCourse(Course course) {
        CourseFullDetailsDto dto = new CourseFullDetailsDto();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setCreationDate(course.getCreationDate());
        dto.setCreator(course.getCreator());
        dto.setStudents(course.getStudents());
        dto.setTasks(course.getTasks());
        dto.setComments(course.getComments());
        dto.setFiles(course.getFiles());
        return dto;
    }
}
