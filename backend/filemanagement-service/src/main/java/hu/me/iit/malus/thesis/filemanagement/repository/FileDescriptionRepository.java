package hu.me.iit.malus.thesis.filemanagement.repository;

import hu.me.iit.malus.thesis.filemanagement.model.FileDescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * File Description Repository that handles the database operations
 *
 * @author Ilku Krisztián
 * @author Attila Szőke
 */
public interface FileDescriptionRepository extends JpaRepository<FileDescription, Long> {
    List<FileDescription> findAllByUploadedBy(String uploadedBy);
    List<FileDescription> findAllByTagId(Long tagId);
}