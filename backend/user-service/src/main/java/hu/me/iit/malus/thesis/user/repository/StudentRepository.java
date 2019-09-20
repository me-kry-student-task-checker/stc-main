package hu.me.iit.malus.thesis.user.repository;

import hu.me.iit.malus.thesis.user.model.Student;

import java.util.List;

/**
 * Spring Data repository, that handles all db operations for Student objects
 * @author Javorek Dénes
 */
public interface StudentRepository extends UserBaseRepository<Student> {
    List<Student> findAllBy();
}
