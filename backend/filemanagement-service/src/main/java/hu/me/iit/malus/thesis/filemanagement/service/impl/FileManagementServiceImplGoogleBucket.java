package hu.me.iit.malus.thesis.filemanagement.service.impl;


import com.google.cloud.storage.*;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescription;
import hu.me.iit.malus.thesis.filemanagement.repository.FileDescriptionRepository;
import hu.me.iit.malus.thesis.filemanagement.service.FileManagementService;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.FileNotFoundException;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.UnsupportedOperationException;
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
 * Google bucket based implementation for File management service.
 *
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
     */
    @Override
    public FileDescription uploadFile(Part file, hu.me.iit.malus.thesis.filemanagement.model.Service service, String user, Long tagId) throws IOException {
        String userHash = hashIt(user);
        String fileName = userHash + "_" + file.getSubmittedFileName();
        Blob blob = storage.create(BlobInfo.newBuilder(BUCKET_NAME, service.toString().toLowerCase() + "/" + fileName).setContentType(file.getContentType()).build(), file.getInputStream());
        log.debug("File successfully uploaded: {}", file.getSubmittedFileName());
        FileDescription fileDescription = new FileDescription();
        fileDescription.setUploadDate(new Date());
        fileDescription.setName(fileName);
        fileDescription.setDownloadLink(blob.getMediaLink());
        fileDescription.setSize(file.getSize());
        fileDescription.setContentType(file.getContentType());
        fileDescription.setUploadedBy(user);
        fileDescription.setServices(new HashSet<>());
        fileDescription.getServices().add(service);
        fileDescription.setTagId(tagId);

        for (FileDescription fd : fileDescriptionRepository.findAll()) {
            if (fd.getName().equalsIgnoreCase(fileDescription.getName())) {
                fileDescription.setId(fd.getId());
                fileDescription.getServices().addAll(fd.getServices());
                break;
            }
        }
        fileDescriptionRepository.save(fileDescription);
        log.debug("File description successfully saved to database: {}", fileDescription);
        fileDescription.setName(file.getSubmittedFileName());
        return fileDescription;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFile(Long id, hu.me.iit.malus.thesis.filemanagement.model.Service service, String username) throws UnsupportedOperationException, FileNotFoundException {
        FileDescription fileToBeRemoved = fileDescriptionRepository.findById(id).orElseThrow(() -> {
            log.debug("No file was found with the following id: {}", id);
            return new FileNotFoundException();
        });

        if (!fileToBeRemoved.getUploadedBy().equalsIgnoreCase(username)) {
            log.warn("User does not have the privilege to delete file: {}", id);
            throw new UnsupportedOperationException();
        }

        BlobId blobId = BlobId.of(BUCKET_NAME, service.toString().toLowerCase() + "/" + fileToBeRemoved.getName());
        
        boolean deleteSuccessful = storage.delete(blobId);
        if (deleteSuccessful) {
            fileToBeRemoved.getServices().remove(service);
            if (fileToBeRemoved.getServices().isEmpty()) {
                fileDescriptionRepository.delete(fileToBeRemoved);
            } else {
                fileDescriptionRepository.save(fileToBeRemoved);
            }
            log.debug("File successfully deleted: {}, {}", id, service);
        } else {
            log.error("File could not be deleted: {}", id);
            throw new FileNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<FileDescription> getAllFilesByUser(String userEmail) {
        Iterable<FileDescription> fileDescriptionList = fileDescriptionRepository.findAllByUploadedBy(userEmail);
        Set<FileDescription> results = new HashSet<>();
        for (FileDescription fd : fileDescriptionList) {
            results.add(fd);
        }
        log.debug("Files found by file name: {}", userEmail);
        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<FileDescription> getAllFilesByServiceAndTagId(Long tagId, hu.me.iit.malus.thesis.filemanagement.model.Service service) {
        List<FileDescription> fileDescriptions = fileDescriptionRepository.findAllByTagId(tagId);
        Set<FileDescription> results = new HashSet<>();
        for (FileDescription fd : fileDescriptions) {
            if (fd.getServices().contains(service)) {
                results.add(fd);
            }
        }
        return results;
    }

    @Override
    public File getFileByName(String name) {
        // no implementation as it is not needed when google buckets are used
        return null;
    }

    /**
     * Removes the generated hash from the file name
     *
     * @param fileName  The file name that contains the hash
     * @param userEmail The user that uploaded the file, and also hash is generated from this
     * @return The original file name that the user uploaded in the first place
     */
    private String removeHash(String fileName, String userEmail) {
        String hashedUser = hashIt(userEmail);
        if (hashedUser != null) {
            fileName = fileName.replace(hashedUser, "");
            return fileName.substring(1);
        }

        return fileName;
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
}
