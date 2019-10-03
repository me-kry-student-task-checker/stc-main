package hu.me.iit.malus.thesis.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.me.iit.malus.thesis.course.client.dto.Teacher;
import hu.me.iit.malus.thesis.course.model.Course;
import hu.me.iit.malus.thesis.course.security.config.JwtAuthConfig;
import hu.me.iit.malus.thesis.course.service.CourseService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CourseController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class CourseControllerTest {
    @TestConfiguration
    static class CourseControllerTestContextConfiguration {
        @Bean
        public JwtAuthConfig jwtAuthConfig() {
            return new JwtAuthConfig();
        }
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseService courseService;

    private MockMvc mvc;

    @Before
    public void setUp() {
        // This is used to bypass Spring Security, as passing JWT filter would be difficult.
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void givenEmployees_whenGetEmployees_thenReturnJsonArray()
            throws Exception {
        // Given
        Teacher courseOwner = new Teacher(
                "teacher@test.test", "Teacher", "Test", new ArrayList<>(), true);
        Course course = new Course("Meant To Be Created", "Creation tester", courseOwner);

        given(courseService.create(course)).willReturn(course);

        // When
        MvcResult response = mvc.perform(post("/api/course/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isOk())
                .andReturn();
        Course responseCourse = objectMapper.readValue(response.getResponse().getContentAsString(), Course.class);

        // Then
        Assertions.assertThat(course).isEqualTo(responseCourse);
    }
}
