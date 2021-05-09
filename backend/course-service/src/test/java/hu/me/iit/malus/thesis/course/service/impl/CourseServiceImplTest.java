package hu.me.iit.malus.thesis.course.service.impl;

import hu.me.iit.malus.thesis.course.client.FeedbackClient;
import hu.me.iit.malus.thesis.course.client.FileManagementClient;
import hu.me.iit.malus.thesis.course.client.TaskClient;
import hu.me.iit.malus.thesis.course.client.UserClient;
import hu.me.iit.malus.thesis.course.controller.dto.CourseCreateDto;
import hu.me.iit.malus.thesis.course.controller.dto.CourseOverviewDto;
import hu.me.iit.malus.thesis.course.model.Course;
import hu.me.iit.malus.thesis.course.repository.CourseRepository;
import hu.me.iit.malus.thesis.course.service.converters.Converter;
import hu.me.iit.malus.thesis.dto.Teacher;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

/**
 * @author Javorek Dénes
 */
@RunWith(SpringRunner.class)
public class CourseServiceImplTest {
    @Mock private CourseRepository courseRepository;
    @Mock private TaskClient taskClient;
    @Mock private FeedbackClient feedbackClient;
    @Mock private UserClient userClient;
    @Mock private FileManagementClient fileManagementClient;

    @InjectMocks
    private CourseServiceImpl courseService;

    @After
    public void reset() {
        Mockito.reset(courseRepository);
        Mockito.reset(taskClient);
        Mockito.reset(feedbackClient);
        Mockito.reset(userClient);
        Mockito.reset(fileManagementClient);
    }

    @Test
    public void whenCreate_ProperCallsAndReturn_happyPath() {
        // Given
        String courseOwnersEmail = "teacher@test.teacher";
        Teacher courseOwner = new Teacher(
                courseOwnersEmail, "Teacher", "Test", new ArrayList<>(), true);
        CourseCreateDto courseCreateDto = new CourseCreateDto();
        courseCreateDto.setName("Meant To Be Created");
        courseCreateDto.setDescription("Creation tester");
        Course course = new Course("Meant To Be Created", "Creation tester", courseOwner);

        Mockito.when(courseRepository.save(Converter.createCourseFromCourseCreateDto(courseCreateDto))).thenReturn(course);
        Mockito.when(userClient.getTeacherByEmail(courseOwnersEmail)).thenReturn(courseOwner);

        // When
        CourseOverviewDto createdCourse = courseService.create(courseCreateDto, courseOwnersEmail);

        // Then
        Mockito.verify(courseRepository, Mockito.times(1)).save(course);
        Mockito.verify(userClient, Mockito.times(1)).saveCourseCreation(null);
        Assertions.assertThat(createdCourse.getName()).isEqualTo(course.getName());
        Assertions.assertThat(createdCourse.getDescription()).isEqualTo(course.getDescription());
    }
}
