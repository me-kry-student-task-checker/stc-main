package hu.me.iit.malus.thesis.filemanagement.service.impl;


import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import hu.me.iit.malus.thesis.filemanagement.controller.dto.FileDescriptorDto;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescriptor;
import hu.me.iit.malus.thesis.filemanagement.model.ServiceType;
import hu.me.iit.malus.thesis.filemanagement.repository.FileDescriptorRepository;
import hu.me.iit.malus.thesis.filemanagement.service.converters.Converter;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.FileNotFoundException;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.ForbiddenFileDeleteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Default implementation for FileDescription management service.
 *
 * @author Ilku Krisztian
 **/
@Service
@Slf4j
@Profile("google")
public class FileManagementServiceImplGoogleBucket extends FileManagementServiceImplBase {

    private static final Storage storage = StorageOptions.getDefaultInstance().getService();
    @Value("${google-cloud-bucket-name}")
    private String BUCKET_NAME;

    private final RedisTemplate<String, List<Long>> redisTemplate;
    public FileManagementServiceImplGoogleBucket(
        FileDescriptorRepository fileDescriptorRepository, RedisTemplate<String, List<Long>> redisTemplate) {
        super(fileDescriptorRepository);
        this.redisTemplate = redisTemplate;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public FileDescriptorDto uploadFile(MultipartFile file, ServiceType serviceType, String userEmail, Long tagId) throws IOException {
        String userHash = hashIt(userEmail);
        String fileName = String.format("%s_%s", userHash, file.getName());
        Blob blob = storage.create(
                BlobInfo.newBuilder(BUCKET_NAME, serviceType.toString().toLowerCase() + "/" + fileName).setContentType(file.getContentType()).build(),
                file.getInputStream().readAllBytes()
        );
        log.debug("File successfully uploaded to google bucket: {}", file.getName());
        FileDescriptor fileDescriptor = new FileDescriptor(
                null, fileName, blob.getMediaLink(), file.getSize(), new Date(), userEmail, file.getContentType(), serviceType, tagId, false);
        fileDescriptorRepository.save(fileDescriptor);
        log.debug("File description successfully saved to database: {}", fileDescriptor);
        fileDescriptor.setName(file.getName());
        return Converter.createFileDescriptorDtoFromFileDescriptor(fileDescriptor);
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
    public List<FileDescriptorDto> getAllFilesByUser(String userEmail) {
        List<FileDescriptor> results = fileDescriptorRepository.findAllByUploadedByAndRemovedFalse(userEmail);
        log.debug("Files found by user {}: {}", userEmail, results);
        return Converter.createFileDescriptorDtoListFromFileDescriptorList(results);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FileDescriptorDto> getAllFilesByServiceTypeAndTagId(Long tagId, ServiceType serviceType) {
        List<FileDescriptor> results = fileDescriptorRepository.findAllByServiceTypeAndTagIdAndRemovedFalse(serviceType, tagId);
        log.debug("Files found by file service {} and tagId {}: {}", serviceType, tagId, results);
        return Converter.createFileDescriptorDtoListFromFileDescriptorList(results);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path getFileByName(String name) {
        // no implementation as it is not needed nor called when google buckets are used
        return null;
    }

    /**
     * Hashes the given parameter
     *
     * @param userEmail The string to be hashed
     * @return The hashed value of the given parameter
     */
    private String hashIt(String userEmail) {
        MessageDigest mDigest;
        try {
            mDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
            return null;
        }

        byte[] bytes = mDigest.digest(userEmail.getBytes(StandardCharsets.UTF_8));
        var stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02X", b));
        }
        return stringBuilder.toString();
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
        List<Long> fileDescriptorIds = fileDescriptors.stream().map(FileDescriptor::getId).collect(Collectors.toList());
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
