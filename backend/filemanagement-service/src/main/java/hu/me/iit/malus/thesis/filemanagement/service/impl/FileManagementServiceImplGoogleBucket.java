package hu.me.iit.malus.thesis.filemanagement.service.impl;


import com.google.cloud.storage.*;
import hu.me.iit.malus.thesis.filemanagement.controller.dto.FileDescriptorDto;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescriptor;
import hu.me.iit.malus.thesis.filemanagement.repository.FileDescriptionRepository;
import hu.me.iit.malus.thesis.filemanagement.service.FileManagementService;
import hu.me.iit.malus.thesis.filemanagement.service.converters.Converter;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.FileNotFoundException;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.ForbiddenFileDeleteException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Default implementation for FileDescription management service.
 * @author Ilku Krisztian
 **/
@Service
@Slf4j
@Profile("google")
@RequiredArgsConstructor
public class FileManagementServiceImplGoogleBucket implements FileManagementService {


    private static final Storage storage = StorageOptions.getDefaultInstance().getService();
    @Value("${google-cloud-bucket-name}")
    private String BUCKET_NAME;
    private final FileDescriptionRepository fileDescriptionRepository;

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public FileDescriptorDto uploadFile(Part file, hu.me.iit.malus.thesis.filemanagement.model.Service service, String user, Long tagId) throws IOException {
        String userHash = hashIt(user);
        String fileName = userHash + "_" + file.getSubmittedFileName();
        Blob blob = storage.create(BlobInfo.newBuilder(BUCKET_NAME, service.toString().toLowerCase() + "/" + fileName).setContentType(file.getContentType()).build(), file.getInputStream());
        log.debug("File successfully uploaded: {}", file.getSubmittedFileName());
        FileDescriptor fileDescriptor = new FileDescriptor();
        fileDescriptor.setUploadDate(new Date());
        fileDescriptor.setName(fileName);
        fileDescriptor.setDownloadLink(blob.getMediaLink());
        fileDescriptor.setSize(file.getSize());
        fileDescriptor.setContentType(file.getContentType());
        fileDescriptor.setUploadedBy(user);
        fileDescriptor.setServices(new HashSet<>());
        fileDescriptor.getServices().add(service);
        fileDescriptor.setTagId(tagId);

        for (FileDescriptor fd : fileDescriptionRepository.findAll()) {
            if (fd.getName().equalsIgnoreCase(fileDescriptor.getName())) {
                fileDescriptor.setId(fd.getId());
                fileDescriptor.getServices().addAll(fd.getServices());
                break;
            }
        }
        fileDescriptionRepository.save(fileDescriptor);
        log.debug("File description successfully saved to database: {}", fileDescriptor);
        fileDescriptor.setName(file.getSubmittedFileName());
        return Converter.createFileDescriptorDtoFromFileDescriptor(fileDescriptor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFile(Long id, hu.me.iit.malus.thesis.filemanagement.model.Service service, String username) throws ForbiddenFileDeleteException, FileNotFoundException {
        FileDescriptor fileToBeRemoved = fileDescriptionRepository.findById(id).orElseThrow(() -> {
            log.debug("No file was found with the following id: {}", id);
            return new FileNotFoundException();
        });

        if (!(userRole.equals("ROLE_Teacher") || fileDescription.getUploadedBy().equalsIgnoreCase(email))) {
            log.warn("User does not have the privilege to delete file: {}", id);
            throw new ForbiddenFileDeleteException();
        }
        BlobId blobId = BlobId.of(BUCKET_NAME, service.toString().toLowerCase() + "/" + fileDescription.getName());
        boolean deleteSuccessful = storage.delete(blobId);
        if (deleteSuccessful) {
            fileDescription.getServices().remove(service);
            if (fileDescription.getServices().isEmpty()) {
                fileDescriptionRepository.delete(fileDescription);
            } else {
                fileDescriptionRepository.save(fileDescription);
            }
            log.debug("File successfully deleted: {}, {}", id, service);
        } else {
            log.error("File could not be deleted: {}", id);
            throw new FileNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public Set<FileDescriptorDto> getAllFilesByUser(String userEmail) {
        Iterable<FileDescriptor> fileDescriptionList = fileDescriptionRepository.findAllByUploadedBy(userEmail);
        Set<FileDescriptor> results = new HashSet<>();
        for (FileDescriptor fd : fileDescriptionList) {
            results.add(fd);
        }
        log.debug("Files found by file name: {}", userEmail);
        return Converter.createFileDescriptorDtosFromFileDescriptors(results);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public Set<FileDescriptorDto> getAllFilesByServiceAndTagId(Long tagId, hu.me.iit.malus.thesis.filemanagement.model.Service service) {
        List<FileDescriptor> fileDescriptors = fileDescriptionRepository.findAllByTagId(tagId);
        Set<FileDescriptor> results = new HashSet<>();
        for (FileDescriptor fd : fileDescriptors) {
            if (fd.getServices().contains(service)) {
                results.add(fd);
            }
        }
        return Converter.createFileDescriptorDtosFromFileDescriptors(results);
    }

    @Override
    public File getFileByName(String name) {
        // no implementation as it is not needed when google buckets are used
        return null;
    }

    /**
     * Hashes the given parameter
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
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02X", b));
        }
        return stringBuilder.toString();
    }

    @Override
    public void deleteFilesByServiceAndTagId(hu.me.iit.malus.thesis.filemanagement.model.Service service, Long tagId, String email, String userRole)
            throws FileNotFoundException, UnsupportedOperationException {
        List<FileDescription> fileDescriptions = fileDescriptionRepository.findAllByServicesContainingAndTagId(service, tagId);
        for (FileDescription fileDescription : fileDescriptions) {
            deleteFile(fileDescription.getId(), service, email, userRole);
        }
    }
}
