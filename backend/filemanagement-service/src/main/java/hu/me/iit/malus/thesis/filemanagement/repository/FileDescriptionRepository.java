package hu.me.iit.malus.thesis.filemanagement.repository;

import hu.me.iit.malus.thesis.filemanagement.model.FileDescription;
import org.springframework.data.repository.CrudRepository;

public interface FileDescriptionRepository extends CrudRepository<FileDescription, Long> {
    Iterable<FileDescription> findAllByName(String fileName);
}
