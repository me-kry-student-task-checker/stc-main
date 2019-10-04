package hu.me.iit.malus.thesis.course.service.impl;

import hu.me.iit.malus.thesis.course.client.FeedbackClient;
import hu.me.iit.malus.thesis.course.client.FileManagementClient;
import hu.me.iit.malus.thesis.course.client.TaskClient;
import hu.me.iit.malus.thesis.course.client.UserClient;
import hu.me.iit.malus.thesis.course.client.dto.Teacher;
import hu.me.iit.malus.thesis.course.model.Course;
import hu.me.iit.malus.thesis.course.repository.CourseRepository;
import hu.me.iit.malus.thesis.course.repository.InvitationRepository;
import hu.me.iit.malus.thesis.course.service.CourseService;
import org.assertj.core.api.Assertions;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
public class CourseServiceImplTest {
    @TestConfiguration
    static class CourseServiceImplTestContextConfiguration {
        @Bean
        @Autowired
        public CourseService courseService(CourseRepository courseRepository, InvitationRepository invitationRepository,
                                           TaskClient taskClient, FeedbackClient feedbackClient, UserClient userClient,
                                           FileManagementClient fileManagementClient) {
            return new CourseServiceImpl(courseRepository, invitationRepository, taskClient, feedbackClient, userClient,
                    fileManagementClient);
        }
    }

    @MockBean private CourseRepository courseRepository;
    @MockBean private InvitationRepository invitationRepository;
    @MockBean private TaskClient taskClient;
    @MockBean private FeedbackClient feedbackClient;
    @MockBean private UserClient userClient;
    @MockBean private FileManagementClient fileManagementClient;

    @Autowired
    private CourseService courseService;

    @After
    public void reset() {
        Mockito.reset(courseRepository);
        Mockito.reset(invitationRepository);
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
        Course course = new Course("Meant To Be Created", "Creation tester", courseOwner);

        Mockito.when(courseRepository.save(course)).thenReturn(course);
        Mockito.when(userClient.getTeacherByEmail(courseOwnersEmail)).thenReturn(courseOwner);

        // When
        Course createdCourse = courseService.create(course);

        // Then
        Mockito.verify(courseRepository, Mockito.times(1)).save(course);
        Mockito.verify(userClient, Mockito.times(1)).saveTeacher(courseOwner);
        Assertions.assertThat(createdCourse).isEqualTo(course);
    }
}
