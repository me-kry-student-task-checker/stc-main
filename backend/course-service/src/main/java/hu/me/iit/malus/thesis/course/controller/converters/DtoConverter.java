package hu.me.iit.malus.thesis.course.controller.converters;

import hu.me.iit.malus.thesis.course.client.dto.Teacher;
import hu.me.iit.malus.thesis.course.controller.dto.CourseDto;
import hu.me.iit.malus.thesis.course.model.Course;

/**
 * Collection of static Dto converter methods.
 * @author Javorek Dénes
 */
public class DtoConverter {
    public static Course CourseDtoToCourse(CourseDto courseDto) {
        Teacher courseCreator = new Teacher();

        Course course = new Course();
        course.setName(course.getName());
        course.setDescription(course.getDescription());
        course.setCreator(courseCreator.withEmail(courseDto.getCreator()));

        return course;
    }
}
