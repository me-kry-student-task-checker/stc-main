package hu.me.iit.malus.thesis.course.controller.converters;

import hu.me.iit.malus.thesis.course.controller.dto.CourseModificationDto;
import hu.me.iit.malus.thesis.course.controller.dto.CourseOverviewDto;
import hu.me.iit.malus.thesis.course.model.Course;

import java.util.ArrayList;
import java.util.List;

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

    public static List<CourseOverviewDto> CourseToCourseOverviewList(List<Course> courseList) {
        List<CourseOverviewDto> courseOverviewList= new ArrayList<>();

        for (Course course : courseList) {
            CourseOverviewDto courseOverview = new CourseOverviewDto();
            courseOverview.setId(course.getId());
            courseOverview.setName(course.getName());
            courseOverview.setDescription(course.getDescription());
            courseOverview.setCreationDate(course.getCreationDate());
            courseOverview.setCreator(course.getCreator());
            courseOverviewList.add(courseOverview);
        }

        return courseOverviewList;
    }
}
