package hu.me.iit.malus.thesis.feedback.repository;

import hu.me.iit.malus.thesis.feedback.model.TaskComment;

import java.util.List;
import java.util.Optional;

public interface TaskCommentRepository extends CommentBaseRepository<TaskComment> {

    Optional<List<TaskComment>> findAllByTaskId(Long courseId);
}
