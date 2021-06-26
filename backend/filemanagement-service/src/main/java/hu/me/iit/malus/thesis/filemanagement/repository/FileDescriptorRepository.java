package hu.me.iit.malus.thesis.filemanagement.repository;

import hu.me.iit.malus.thesis.filemanagement.model.FileDescriptor;
import hu.me.iit.malus.thesis.filemanagement.model.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * File Description Repository that handles the database operations
 *
 * @author Ilku Krisztián
 * @author Attila Szőke
 */
public interface FileDescriptorRepository extends JpaRepository<FileDescriptor, Long> {

    List<FileDescriptor> findAllByUploadedBy(String uploadedBy);

    List<FileDescriptor> findAllByTagId(Long tagId);

    List<FileDescriptor> findAllByServiceTypeAndTagId(ServiceType serviceType, Long tagId);
}