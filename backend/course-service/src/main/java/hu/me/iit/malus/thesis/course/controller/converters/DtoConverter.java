package hu.me.iit.malus.thesis.course.controller.converters;

import hu.me.iit.malus.thesis.course.controller.dto.CourseModificationDto;
import hu.me.iit.malus.thesis.course.controller.dto.CourseOverviewDto;
import hu.me.iit.malus.thesis.course.model.Course;

import java.util.HashSet;
import java.util.Set;

/**
 * Collection of static Dto converter methods.
 * @author Javorek Dénes
 */
public class DtoConverter {
    public static Course CourseDtoToCourse(CourseModificationDto courseModificationDto) {
        Course course = new Course();

        course.setId(courseModificationDto.getId());
        course.setName(courseModificationDto.getName());
        course.setDescription(courseModificationDto.getDescription());

        return course;
    }

    public static Set<CourseOverviewDto> CourseToCourseOverviewSet(Set<Course> courseSet) {
        Set<CourseOverviewDto> courseOverviewSet= new HashSet<>();

        for (Course course : courseSet) {
            CourseOverviewDto courseOverview = new CourseOverviewDto();
            courseOverview.setId(course.getId());
            courseOverview.setName(course.getName());
            courseOverview.setDescription(course.getDescription());
            courseOverview.setCreationDate(course.getCreationDate());
            courseOverview.setCreator(course.getCreator());
            courseOverviewSet.add(courseOverview);
        }

        return courseOverviewSet;
    }
}
