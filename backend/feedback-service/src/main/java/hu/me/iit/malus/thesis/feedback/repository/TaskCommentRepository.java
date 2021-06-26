package hu.me.iit.malus.thesis.feedback.repository;

import hu.me.iit.malus.thesis.feedback.model.TaskComment;

import java.util.List;

public interface TaskCommentRepository extends CommentBaseRepository<TaskComment> {

    List<TaskComment> findAllByTaskId(Long courseId);

    List<TaskComment> deleteByTaskId(Long taskId);
}
