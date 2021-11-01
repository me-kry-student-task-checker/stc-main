package hu.me.iit.malus.thesis.filemanagement.repository;

import hu.me.iit.malus.thesis.filemanagement.model.FileDescriptor;
import hu.me.iit.malus.thesis.filemanagement.model.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * File Description Repository that handles the database operations
 *
 * @author Ilku Krisztián
 * @author Attila Szőke
 */
public interface FileDescriptorRepository extends JpaRepository<FileDescriptor, Long> {

    Optional<FileDescriptor> findByIdAndRemovedFalse(Long id);

    List<FileDescriptor> findAllByUploadedByAndRemovedFalse(String uploadedBy);

    List<FileDescriptor> findAllByServiceTypeAndTagIdAndRemovedFalse(ServiceType serviceType, Long tagId);

    List<FileDescriptor> findAllByServiceTypeAndTagIdInAndRemovedFalse(ServiceType serviceType, List<Long> tagIds);
}