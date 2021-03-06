package hu.me.iit.malus.thesis.feedback.repository;

import hu.me.iit.malus.thesis.feedback.model.CourseComment;

import java.util.List;

public interface CourseCommentRepository extends CommentBaseRepository<CourseComment> {

    List<CourseComment> findAllByCourseId(Long courseId);

    List<CourseComment> deleteByCourseId(Long courseId);
}
