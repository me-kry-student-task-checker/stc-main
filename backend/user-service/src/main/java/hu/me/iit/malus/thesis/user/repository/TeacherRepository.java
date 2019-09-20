package hu.me.iit.malus.thesis.user.repository;

import hu.me.iit.malus.thesis.user.model.Teacher;

import java.util.List;

/**
 * Spring Data repository, that handles all db operations for Teacher objects
 * @author Javorek Dénes
 */
public interface TeacherRepository extends UserBaseRepository<Teacher> {
    List<Teacher> findAllBy();
}
