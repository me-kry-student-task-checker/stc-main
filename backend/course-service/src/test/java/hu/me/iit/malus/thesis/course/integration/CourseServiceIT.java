package hu.me.iit.malus.thesis.course.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import hu.me.iit.malus.thesis.course.CourseServiceApplication;
import hu.me.iit.malus.thesis.course.client.dto.Teacher;
import hu.me.iit.malus.thesis.course.controller.helper.JwtTestHelper;
import hu.me.iit.malus.thesis.course.model.Course;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CourseServiceApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class CourseServiceIT {
    @TestConfiguration
    public static class LocalRibbonClientConfiguration {

    }

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JwtTestHelper jwtHelper;


    @Test
    public void whenCreateCourse_WithValidToken_returnOkAndTheSameCourse()
            throws Exception {
        // Given
        Teacher courseOwner = new Teacher(
                "teacher@test.test", "Teacher", "Test", new ArrayList<>(), true);
        Course course = new Course("Meant To Be Created", "Creation tester", courseOwner);


        // When
        MvcResult response = mvc.perform(post("/api/course/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(course))
                .header(jwtHelper.getJwtHeader(), jwtHelper.createValidJWT("teacher", "TEACHER")))
                .andExpect(status().isOk())
                .andReturn();
        Course responseCourse = objectMapper.readValue(response.getResponse().getContentAsString(), Course.class);

        // Then
        Assertions.assertThat(course).isEqualTo(responseCourse);
    }
}
