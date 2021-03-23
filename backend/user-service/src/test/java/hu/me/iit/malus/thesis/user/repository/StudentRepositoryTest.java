package hu.me.iit.malus.thesis.user.repository;

import hu.me.iit.malus.thesis.user.model.Student;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * @author Javorek Dénes
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class StudentRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository repository;

    @Before
    public void reset() {
        entityManager.clear();
    }

    @Test
    public void whenFindAllByAssignedCourseId_withExistingId_returnSetOfOne() {
        // Given
        Long criteriaCourseId = 54L;
        List<Long> someIdsOfAssignedCourses = Arrays.asList(1L, 2L, 6L, criteriaCourseId, 5L, 60L);

        Student studentToFind = new Student(
                "toFind@teacher.test", "$2aHash", "Target", "Student", new ArrayList<>(), null);
        studentToFind.setAssignedCourseIds(someIdsOfAssignedCourses);

        Student studentToNotToFind = new Student(
                "nobody@student.test", "$2aHash", "Nobody", "Student", new ArrayList<>(), null);

        entityManager.persist(studentToFind);
        entityManager.persist(studentToNotToFind);
        entityManager.flush();

        // When
        Set<Student> foundByAssignedCourseId = new HashSet<>(repository.findAllAssignedForCourseId(criteriaCourseId));

        // Then
        Assertions.assertThat(foundByAssignedCourseId.size()).isEqualTo(1);
        Assertions.assertThat(foundByAssignedCourseId.contains(studentToFind)).isEqualTo(true);
    }

    @Test
    public void whenFindAllByAssignedCourseId_withNotExistingId_returnEmptySet() {
        // Given
        Long criteriaCourseId_missing = 54L;
        List<Long> someIdsOfCreatedCourses = Arrays.asList(1L, 2L, 6L, 5L, 60L);

        Student studentToNotToFind = new Student(
                "nobody@student.test", "$2aHash", "Nobody", "Student", new ArrayList<>(), null);
        studentToNotToFind.setAssignedCourseIds(someIdsOfCreatedCourses);

        entityManager.persist(studentToNotToFind);
        entityManager.flush();

        // When
        Set<Student> foundByAssignedCourseId = new HashSet<>(repository.findAllAssignedForCourseId(criteriaCourseId_missing));

        // Then
        Assertions.assertThat(foundByAssignedCourseId.size()).isEqualTo(0);
    }

    @Test
    public void whenFindAllByNotAssignedCourseId_withExistingId_returnNotAssigned() {
        // Given
        Long criteriaCourseId = 54L;
        List<Long> someIdsOfAssignedCourses = Arrays.asList(1L, 2L, 6L, criteriaCourseId, 5L, 60L);

        Student studentToFind = new Student(
                "toFind@teacher.test", "$2aHash", "Target", "Student", new ArrayList<>(), null);

        Student studentNotToFind = new Student(
                "nobody@student.test", "$2aHash", "Nobody", "Student", new ArrayList<>(), null);
        studentNotToFind.setAssignedCourseIds(someIdsOfAssignedCourses);

        entityManager.persist(studentToFind);
        entityManager.persist(studentNotToFind);
        entityManager.flush();

        // When
        Set<Student> foundByAssignedCourseId = new HashSet<>(repository.findAllNotAssignedForCourseId(criteriaCourseId));

        // Then
        Assertions.assertThat(foundByAssignedCourseId.size()).isEqualTo(1);
        Assertions.assertThat(foundByAssignedCourseId.contains(studentToFind)).isEqualTo(true);
    }
}
