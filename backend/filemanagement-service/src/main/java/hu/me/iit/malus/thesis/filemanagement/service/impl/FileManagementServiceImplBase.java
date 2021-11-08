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
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public abstract class FileManagementServiceImplBase implements FileManagementService{

  protected final FileDescriptorRepository fileDescriptorRepository;

  protected final RedisTemplate<String, List<Long>> redisTemplate;

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

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteFile(Long id, ServiceType serviceType, String email, String userRole)
      throws ForbiddenFileDeleteException, FileNotFoundException {
    FileDescriptor fileDescriptor = fileDescriptorRepository.findByIdAndRemovedFalse(id).orElseThrow(() -> {
      log.debug("No file was found with the following id: {}", id);
      return new FileNotFoundException();
    });
    if (!(userRole.equals("ROLE_Teacher") || fileDescriptor.getUploadedBy().equals(email))) {
      log.warn("User: {} a {} does not have the privilege: to delete file {}", email, userRole, id);
      throw new ForbiddenFileDeleteException();
    }
    fileDescriptor.setRemoved(true);
    fileDescriptorRepository.save(fileDescriptor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public String prepareRemoveFilesByServiceAndTagId(ServiceType serviceType, List<Long> tagIds) {
    List<FileDescriptor> fileDescriptors = fileDescriptorRepository.findAllByServiceTypeAndTagIdInAndRemovedFalse(serviceType, tagIds);
    fileDescriptors.forEach(fileDescriptor -> fileDescriptor.setRemoved(true));
    fileDescriptorRepository.saveAll(fileDescriptors);
    String uuid = UUID.randomUUID().toString();
    List<Long> fileDescriptorIds = fileDescriptors.stream().map(FileDescriptor::getId).collect(
        Collectors.toList());
    redisTemplate.opsForValue().set(uuid, fileDescriptorIds);
    log.debug("Prepared ids: {}, for removal with {} transaction key!", fileDescriptorIds, uuid);
    return uuid;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void commitRemoveFilesByServiceAndTagId(String transactionKey) {
    boolean success = redisTemplate.delete(transactionKey);
    log.debug("Committed transaction with key: {}, delete successful: {}!", transactionKey, success);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void rollbackRemoveFilesByServiceAndTagId(String transactionKey) {
    List<Long> fileDescriptorIds = redisTemplate.opsForValue().get(transactionKey);
    if (fileDescriptorIds == null) {
      log.debug("Cannot find transaction key in Redis, like this: '{}'!", transactionKey);
      return;
    }
    List<FileDescriptor> fileDescriptors = fileDescriptorRepository.findAllById(fileDescriptorIds);
    fileDescriptors.forEach(task -> task.setRemoved(false));
    fileDescriptorRepository.saveAll(fileDescriptors);
    redisTemplate.delete(transactionKey);
    log.debug("Rolled back transaction with key: {}!", transactionKey);
  }
}
