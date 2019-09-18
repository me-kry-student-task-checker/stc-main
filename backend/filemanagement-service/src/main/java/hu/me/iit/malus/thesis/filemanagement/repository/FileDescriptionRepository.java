package hu.me.iit.malus.thesis.filemanagement.repository;

import hu.me.iit.malus.thesis.filemanagement.controller.dto.Service;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescription;
import org.springframework.data.repository.CrudRepository;

/**
 * File Description Repository that handles the database operations
 */
public interface FileDescriptionRepository extends CrudRepository<FileDescription, Long> {
    Iterable<FileDescription> findAllByName(String fileName);
    Iterable<FileDescription> findAllByServices(Service service);
    Iterable<FileDescription> findAllByUploadedBy(String uploadedBy);
}