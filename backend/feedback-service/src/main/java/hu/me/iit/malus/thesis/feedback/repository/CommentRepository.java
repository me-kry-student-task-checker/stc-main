package hu.me.iit.malus.thesis.feedback.repository;

import hu.me.iit.malus.thesis.feedback.model.Comment;
import hu.me.iit.malus.thesis.feedback.model.CourseComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository<T extends Comment> extends JpaRepository<T, Long> {

    Optional<List<T>> findAllByCourseId(Long courseId);

    Optional<List<T>> findAllByTaskId(Long courseId);
}
