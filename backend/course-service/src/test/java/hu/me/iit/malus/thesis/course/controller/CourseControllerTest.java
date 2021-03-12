package hu.me.iit.malus.thesis.course.controller;

import com.google.gson.Gson;
import hu.me.iit.malus.thesis.course.controller.converters.DtoConverter;
import hu.me.iit.malus.thesis.course.controller.dto.CourseModificationDto;
import hu.me.iit.malus.thesis.course.controller.helper.JwtTestHelper;
import hu.me.iit.malus.thesis.course.model.Course;
import hu.me.iit.malus.thesis.course.security.config.JwtAuthConfig;
import hu.me.iit.malus.thesis.course.service.CourseService;
import hu.me.iit.malus.thesis.dto.Teacher;
import hu.me.iit.malus.thesis.dto.UserRole;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Javorek Dénes
 */
@RunWith(SpringRunner.class)
@WebMvcTest(CourseController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class CourseControllerTest {
    @TestConfiguration
    static class CourseControllerTestContextConfiguration {
        // Default config cannot be instantiated as properties (from config-service) are missing.
        // We defined those properties as test properties, so this bean can be created now.
        @Bean
        public JwtAuthConfig jwtAuthConfig() {
            return new JwtAuthConfig();
        }

        @Bean
        public JwtTestHelper jwtTestHelper() {
            return new JwtTestHelper();
        }

        @Bean
        CourseService courseService(
                 CourseRepository courseRepository,
                 InvitationRepository invitationRepository,
                 TaskClient taskClient,
                 FeedbackClient feedbackClient,
                 UserClient userClient,
                 FileManagementClient fileManagementClient,
                 EmailClient emailClient,
                 InvitationConfig invitationConfig
        ) { return new CourseServiceImpl(courseRepository, invitationRepository, taskClient, feedbackClient,userClient, fileManagementClient, emailClient, invitationConfig);
        }
    }

    @Autowired private MockMvc mvc;
    @Autowired private Gson gson;
    @Autowired private JwtTestHelper jwtHelper;
    @Autowired private CourseRepository courseRepository;

 //   @MockBean private CourseService courseService;

    private final String courseOwnersEmail = "teacher@test.test";

    private Course getCourseForTeacher() {
        Teacher courseOwner = new Teacher(
                courseOwnersEmail, "Teacher", "Test", new ArrayList<>(), true);
        return new Course("Meant To Be Created", "Creation tester", courseOwner);
    }

    private String getTeacherJWT(){
        return jwtHelper.createValidJWT(courseOwnersEmail, UserRole.TEACHER.getRoleString());
    }

    @Test
    public void whenCreateCourse_WithValidToken_returnOkAndTheSameCourse()
            throws Exception {
        // Given
        Course course = getCourseForTeacher();

        CourseModificationDto courseDto = CourseModificationDto.builder()
                .name(course.getName())
                .description(course.getDescription())
                .build();

        // When
        MvcResult response = mvc.perform(post("/api/course/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(courseDto))
                .header(jwtHelper.getJwtHeader(), getTeacherJWT()))
                .andExpect(status().isOk())
                .andReturn();
        Course responseCourse = gson.fromJson(response.getResponse().getContentAsString(), Course.class);

        // Then
        Assertions.assertThat(course).isEqualTo(responseCourse);
        Optional<Course> byId = courseRepository.findById(course.getId());
        Course course1 = byId.get();
        assertThat(course1, notNullValue());
        // assertThat(course1.getId(), greaterThan(0));


    }

    @Test
    public void whenCreateCourse_WithoutToken_returnUnauthorized()
            throws Exception {
        // Given
        Course course = getCourseForTeacher();


      //  given(courseService.create(course, courseOwnersEmail)).willReturn(course);

        // When
        mvc.perform(post("/api/course/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(course)))
                .andExpect(status().isUnauthorized()); //Then
    }

    @Test
    public void whenStudentCreateCourse_WithValidToken_returnForbidden()
            throws Exception {
        // Given
        CourseModificationDto courseDto = CourseModificationDto.builder()
                .name("Meant To Be Created")
                .description("Haha, can I create this one?")
                .build();

        // When
        mvc.perform(post("/api/course/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(courseDto))
                .header(jwtHelper.getJwtHeader(), jwtHelper.createValidJWT("bob", UserRole.STUDENT.getRoleString())))
                .andExpect(status().isForbidden()); // Then
    }

    @Test
    public void whenTeacherCreateCourse_WithInvalidInput_returnBadRequest()
            throws Exception {
        // Given
        CourseModificationDto courseDto = CourseModificationDto.builder().name("Meant To Be Created").build();

        // When
        mvc.perform(post("/api/course/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(courseDto))
                .header(jwtHelper.getJwtHeader(), getTeacherJWT()))
                .andExpect(status().isBadRequest()); // Then
    }
}
