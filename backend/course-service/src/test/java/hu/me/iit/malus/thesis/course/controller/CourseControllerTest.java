package hu.me.iit.malus.thesis.course.controller;

import com.google.gson.Gson;
import hu.me.iit.malus.thesis.course.client.dto.Teacher;
import hu.me.iit.malus.thesis.course.client.dto.UserRole;
import hu.me.iit.malus.thesis.course.controller.converters.DtoConverter;
import hu.me.iit.malus.thesis.course.controller.dto.CourseModificationDto;
import hu.me.iit.malus.thesis.course.controller.helper.JwtTestHelper;
import hu.me.iit.malus.thesis.course.model.Course;
import hu.me.iit.malus.thesis.course.security.config.JwtAuthConfig;
import hu.me.iit.malus.thesis.course.service.CourseService;
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
    }

    @Autowired private MockMvc mvc;
    @Autowired private Gson gson;
    @Autowired private JwtTestHelper jwtHelper;

    @MockBean private CourseService courseService;

    @Test
    public void whenCreateCourse_WithValidToken_returnOkAndTheSameCourse()
            throws Exception {
        // Given
        String courseOwnersEmail = "teacher@test.test";
        Teacher courseOwner = new Teacher(
                courseOwnersEmail, "Teacher", "Test", new ArrayList<>(), true);
        Course course = new Course("Meant To Be Created", "Creation tester", courseOwner);

        CourseModificationDto courseDto = new CourseModificationDto();
        courseDto.setName(course.getName());
        courseDto.setDescription(course.getDescription());

        given(courseService.create(DtoConverter.CourseDtoToCourse(courseDto), courseOwnersEmail)).willReturn(course);

        // When
        MvcResult response = mvc.perform(post("/api/course/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(courseDto))
                .header(jwtHelper.getJwtHeader(), jwtHelper.createValidJWT(courseOwnersEmail, UserRole.TEACHER.getRoleString())))
                .andExpect(status().isOk())
                .andReturn();
        Course responseCourse = gson.fromJson(response.getResponse().getContentAsString(), Course.class);

        // Then
        Assertions.assertThat(course).isEqualTo(responseCourse);
    }

    @Test
    public void whenCreateCourse_WithoutToken_returnUnauthorized()
            throws Exception {
        // Given
        String courseOwnersEmail = "teacher@test.test";
        Teacher courseOwner = new Teacher(
                courseOwnersEmail, "Teacher", "Test", new ArrayList<>(), true);
        Course course = new Course("Meant To Be Created", "Creation tester", courseOwner);

        given(courseService.create(course, courseOwnersEmail)).willReturn(course);

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
        CourseModificationDto courseDto = new CourseModificationDto();
        courseDto.setName("Meant To Be Created");
        courseDto.setDescription("Haha, can I create this one?");

        // When
        mvc.perform(post("/api/course/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(courseDto))
                .header(jwtHelper.getJwtHeader(), jwtHelper.createValidJWT("bob", UserRole.STUDENT.getRoleString())))
                .andExpect(status().isForbidden()); // Then
    }
}
