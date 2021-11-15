package hu.me.iit.malus.thesis.feedback.repository;

import hu.me.iit.malus.thesis.feedback.model.CourseComment;

import java.util.List;
import java.util.Optional;

public interface CourseCommentRepository extends CommentBaseRepository<CourseComment> {

    Optional<CourseComment> findByIdAndRemovedFalse(Long id);

    List<CourseComment> findAllByCourseIdAndRemovedFalse(Long courseId);

    List<CourseComment> findAllByCourseIdInAndRemovedFalse(List<Long> courseId);
}
