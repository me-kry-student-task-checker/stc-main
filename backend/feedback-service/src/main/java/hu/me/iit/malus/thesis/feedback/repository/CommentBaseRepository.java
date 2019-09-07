package hu.me.iit.malus.thesis.feedback.repository;

import hu.me.iit.malus.thesis.feedback.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CommentBaseRepository<T extends Comment> extends JpaRepository<T, Long> {
}
