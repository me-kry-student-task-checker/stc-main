package hu.me.iit.malus.thesis.course.repository;

import hu.me.iit.malus.thesis.course.client.dto.Teacher;
import hu.me.iit.malus.thesis.course.model.Course;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

/**
 * @author Javorek Dénes
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class CourseRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository repository;

    @Before
    public void reset() {
        entityManager.clear();
    }

    @Test
    public void whenFindById_courseFound_wrappedInOptional() {
        // Given
        Teacher courseOwner = new Teacher(
                "teacher@stch.test", "Example", "Teacher", Lists.emptyList(), true);
        Course course = new Course("How to save courser", "Save all of us", courseOwner);
        Long courseId = entityManager.persistAndGetId(course, Long.class);
        entityManager.flush();

        // When
        Optional<Course> foundById = repository.findById(courseId);

        // Then
        Assertions.assertThat(foundById.isPresent()).isEqualTo(true);
        Assertions.assertThat(foundById.get()).isEqualTo(course);
    }

    @Test
    public void whenSave_CourseExists() {
        // Given
        Teacher courseOwner = new Teacher(
                "may@be.com", "Theresa", "May", Lists.emptyList(), true);
        Course course = new Course("Brexit Basics", "Make UK, Great again", courseOwner);
        repository.save(course);

        // When
        Long courseId = entityManager.getId(course, Long.class);
        Course foundById = entityManager.find(Course.class, courseId);

        // Then
        Assertions.assertThat(foundById).isEqualTo(course);
    }
}
