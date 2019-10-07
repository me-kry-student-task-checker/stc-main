package hu.me.iit.malus.thesis.user.repository;

import hu.me.iit.malus.thesis.user.model.Teacher;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Javorek Dénes
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class TeacherRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TeacherRepository repository;

    @Before
    public void reset() {
        entityManager.clear();
    }

    @Test
    public void whenFindByCreatedCourseId_withExistingId_foundWrappedInOptional() {
        // Given
        Long criteriaCourseId = 54L;
        List<Long> someIdsOfCreatedCourses = Arrays.asList(1L, 2L, 6L, criteriaCourseId, 5L, 60L);

        Teacher teacherToFind = new Teacher(
                "toFind@teacher.test", "$2aHash", "Target", "Teacher", new ArrayList<>());
        teacherToFind.setCreatedCourseIds(someIdsOfCreatedCourses);

        Teacher teacherToNotToFind = new Teacher(
                "nobody@teacher.test", "$2aHash", "Nobody", "Teacher", new ArrayList<>());

        entityManager.persist(teacherToFind);
        entityManager.persist(teacherToNotToFind);
        entityManager.flush();

        // When
        Optional<Teacher> foundByCreatedCourseId = repository.findByCreatedCourseId(criteriaCourseId);

        // Then
        Assertions.assertThat(foundByCreatedCourseId.isPresent()).isEqualTo(true);
        Assertions.assertThat(foundByCreatedCourseId.get()).isEqualTo(teacherToFind);
    }

    @Test
    public void whenFindByCreatedCourseId_withNotExistingId_returnEmptyOptional() {
        // Given
        Long criteriaCourseId_missing = 54L;
        List<Long> someIdsOfCreatedCourses = Arrays.asList(1L, 2L, 6L, 5L, 60L);

        Teacher teacherNotToFind = new Teacher(
                "toFind@teacher.test", "$2aHash", "Target", "Teacher", new ArrayList<>());
        teacherNotToFind.setCreatedCourseIds(someIdsOfCreatedCourses);

        entityManager.persist(teacherNotToFind);
        entityManager.flush();

        // When
        Optional<Teacher> foundByCreatedCourseId = repository.findByCreatedCourseId(criteriaCourseId_missing);

        // Then
        Assertions.assertThat(foundByCreatedCourseId.isPresent()).isEqualTo(false);
    }
}
