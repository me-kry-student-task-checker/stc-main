package hu.me.iit.malus.thesis.filemanagement.service.impl;


import com.google.cloud.storage.*;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescription;
import hu.me.iit.malus.thesis.filemanagement.repository.FileDescriptionRepository;
import hu.me.iit.malus.thesis.filemanagement.service.FileManagementService;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.FileNotFoundException;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.UnsupportedOperationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Default implementation for FileDescription management service.
 * @author Ilku Krisztian
 **/
@Service
@Slf4j
public class FileManagementServiceImpl implements FileManagementService {


    private static Storage storage = null;
    @Value("${google-cloud-bucket-name}")
    private String BUCKET_NAME;
    private FileDescriptionRepository fileDescriptionRepository;

    @Autowired
    public FileManagementServiceImpl(FileDescriptionRepository fileDescriptionRepository) {
        storage = StorageOptions.getDefaultInstance().getService();
        this.fileDescriptionRepository = fileDescriptionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileDescription uploadFile(Part file, hu.me.iit.malus.thesis.filemanagement.controller.dto.Service service, String user, Long tagId) throws IOException {
        String userHash = hashIt(user);
        String fileName = userHash + "_" + file.getSubmittedFileName();
        Blob blob = storage.create(BlobInfo.newBuilder(BUCKET_NAME, service.toString().toLowerCase() + "/" + fileName).setContentType(file.getContentType()).build(), file.getInputStream());
        log.info("File successfully uploaded: {}", file.getSubmittedFileName());
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
        log.info("File description successfully saved to database: {}", fileDescription);
        fileDescription.setName(file.getSubmittedFileName());
        return fileDescription;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileDescription getById(Long id) {
        boolean isPresent = fileDescriptionRepository.findById(id).isPresent();
        if (isPresent) {
            log.info("File found: {}", id);
            FileDescription result = fileDescriptionRepository.findById(id).get();
            result.setName(removeHash(result.getName(), result.getUploadedBy()));
            return result;
        }

        log.info("File could not be found: {}", id);
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<FileDescription> getAllByFileName(String filename) {
        Iterable<FileDescription> fileDescriptionList = fileDescriptionRepository.findAll();
        Set<FileDescription> results = new HashSet<>();
        for (FileDescription fd : fileDescriptionList) {
            String unHashedFileName = removeHash(fd.getName(), fd.getUploadedBy());
            if (unHashedFileName.equalsIgnoreCase(filename)) {
                fd.setName(unHashedFileName);
                results.add(fd);
            }
        }
        log.info("Files found by file name: {}", filename);
        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<FileDescription> getAllFiles() {
        Iterable<FileDescription> fileDescriptions = fileDescriptionRepository.findAll();
        Set<FileDescription> result = new HashSet<>();
        for (FileDescription i : fileDescriptions) {
            i.setName(removeHash(i.getName(), i.getUploadedBy()));
            result.add(i);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFile(Long id, hu.me.iit.malus.thesis.filemanagement.controller.dto.Service service, String username) throws UnsupportedOperationException, FileNotFoundException {
        boolean fileToBeRemovedIsPresent = fileDescriptionRepository.findById(id).isPresent();
        if (fileToBeRemovedIsPresent) {
            FileDescription fileToBeRemoved = fileDescriptionRepository.findById(id).get();

            if (!fileToBeRemoved.getUploadedBy().equalsIgnoreCase(username)) {
                log.debug("User does not have the privilege to delete file: {}", id);
                throw new UnsupportedOperationException();
            }

            BlobId blobId = BlobId.of(BUCKET_NAME, service.toString().toLowerCase() + "/" + fileToBeRemoved.getName());
            boolean successful_delete = storage.delete(blobId);

            if (successful_delete) {
                FileDescription fd = fileDescriptionRepository.findById(id).get();
                fd.getServices().remove(service);
                if (fd.getServices().isEmpty()) {
                    fileDescriptionRepository.delete(fileToBeRemoved);
                }else {
                    fileDescriptionRepository.save(fd);
                }
                log.info("File successfully deleted: {}, {}", id, service);
            }
            else{
                log.info("File could not be deleted: {}", id);
                log.debug("No file was found with the following id: {}", id);
                throw new FileNotFoundException();
            }
        }else {
            log.debug("No file was found with the following id: {}", id);
            throw new FileNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<FileDescription> getAllFilesByServices(hu.me.iit.malus.thesis.filemanagement.controller.dto.Service service) {
        Set<FileDescription> files = new HashSet<>();
        for (FileDescription fd : fileDescriptionRepository.findAllByServices(service)) {
            fd.setName(removeHash(fd.getName(), fd.getUploadedBy()));
            files.add(fd);
        }
        return files;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<FileDescription> getAllFilesByUsers(String userEmail) {
        Iterable<FileDescription> fileDescriptionList = fileDescriptionRepository.findAllByUploadedBy(userEmail);
        Set<FileDescription> results = new HashSet<>();
        for (FileDescription fd : fileDescriptionList) {
            results.add(fd);
        }
        log.info("Files found by file name: {}", userEmail);
        return results;
    }

    /**
     * {@inheritDoc}
     */
    public Set<FileDescription> getAllFilesByTagId(Long tagId, hu.me.iit.malus.thesis.filemanagement.controller.dto.Service service) {
        Iterable<FileDescription> fileDescriptions = fileDescriptionRepository.findAll();
        Set<FileDescription> results = new HashSet<>();
        for (FileDescription fd : fileDescriptions) {
            if (fd.getTagId().equals(tagId) && fd.getServices().contains(service)) {
                results.add(fd);
            }
        }
        return results;
    }

    /**
     * Removes the generated hash from the file name
     * @param fileName The file name that contains the hash
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
