package hu.me.iit.malus.thesis.filemanagement.service.impl;

import hu.me.iit.malus.thesis.filemanagement.controller.dto.FileDescriptorDto;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescriptor;
import hu.me.iit.malus.thesis.filemanagement.model.ServiceType;
import hu.me.iit.malus.thesis.filemanagement.repository.FileDescriptorRepository;
import hu.me.iit.malus.thesis.filemanagement.service.FileManagementService;
import hu.me.iit.malus.thesis.filemanagement.service.converters.Converter;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.FileNotFoundException;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.ForbiddenFileDeleteException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class FileManagementServiceImplBase implements FileManagementService{

  protected final FileDescriptorRepository fileDescriptorRepository;

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  public List<FileDescriptorDto> getAllFilesByUser(String userEmail) {
    List<FileDescriptor> results = fileDescriptorRepository.findAllByUploadedByAndRemovedFalse(userEmail);
    log.debug("Files found by user {}: {}", userEmail, results);
    return Converter.createFileDescriptorDtoListFromFileDescriptorList(results);
  }


  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  public List<FileDescriptorDto> getAllFilesByServiceTypeAndTagId(Long tagId, ServiceType serviceType) {
    List<FileDescriptor> results = fileDescriptorRepository.findAllByServiceTypeAndTagIdAndRemovedFalse(serviceType, tagId);
    log.debug("Files found by file service {} and tagId {}: {}", serviceType, tagId, results);
    return Converter.createFileDescriptorDtoListFromFileDescriptorList(results);
  }


  @Override
  public void deleteFilesByServiceAndTagId(ServiceType serviceType, Long tagId, String email, String userRole)
      throws FileNotFoundException, UnsupportedOperationException, ForbiddenFileDeleteException {
    List<FileDescriptor> fileDescriptions = fileDescriptorRepository.findAllByServiceTypeAndTagIdAndRemovedFalse(serviceType, tagId);
    for (FileDescriptor fileDescription : fileDescriptions) {
      deleteFile(fileDescription.getId(), serviceType, email, userRole);
    }
  }
}
