package hu.me.iit.malus.thesis.feedback.repository;

import hu.me.iit.malus.thesis.feedback.model.TaskComment;

import java.util.List;
import java.util.Optional;

public interface TaskCommentRepository extends CommentBaseRepository<TaskComment> {

    Optional<TaskComment> findByIdAndRemovedFalse(Long id);

    List<TaskComment> findAllByTaskIdAndRemovedFalse(Long courseId);

    List<TaskComment> deleteByTaskId(Long taskId);
}
