package hu.me.iit.malus.thesis.filemanagement.service.impl;

import hu.me.iit.malus.thesis.filemanagement.model.FileDescription;
import hu.me.iit.malus.thesis.filemanagement.model.Service;
import hu.me.iit.malus.thesis.filemanagement.repository.FileDescriptionRepository;
import hu.me.iit.malus.thesis.filemanagement.service.FileManagementService;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.FileNotFoundException;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.UnsupportedOperationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Local filesystem based implementation for the File management service.
 *
 * @author Attila Szőke
 **/
@org.springframework.stereotype.Service
@Slf4j
@RequiredArgsConstructor
@Profile("!google")
public class FileManagementServiceImplFileSystem implements FileManagementService {

    private static final String FILE_DIR_PROP = "files.upload.dir";

    private final Environment env; // environment is used for retrieving the upload destination from cloud props file, because @Value sets null
    private final FileDescriptionRepository fileDescriptionRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public FileDescription uploadFile(Part file, Service service, String user, Long tagId) throws IOException {
        String fileName = user.hashCode() + "_" + service.hashCode() + "_" + tagId.hashCode() + "_" + file.getSubmittedFileName();
        FileDescription fileDescription = new FileDescription();
        fileDescription.setUploadDate(new Date());
        fileDescription.setName(fileName);
        fileDescription.setDownloadLink("/api/filemanagement/download/link/" + fileName);
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

        String uploadDir = env.getProperty(FILE_DIR_PROP);
        File targetFile = new File(uploadDir + File.separator + fileName);
        FileUtils.copyInputStreamToFile(file.getInputStream(), targetFile);
        log.debug("File successfully uploaded: {}", file.getSubmittedFileName());

        fileDescriptionRepository.save(fileDescription);
        log.debug("File description successfully saved to database: {}", fileDescription);

        return fileDescription;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFile(Long id, Service service, String username) throws UnsupportedOperationException, FileNotFoundException {
        Optional<FileDescription> opt = fileDescriptionRepository.findById(id);
        if (opt.isPresent()) {
            FileDescription fileDescription = opt.get();

            if (!fileDescription.getUploadedBy().equalsIgnoreCase(username)) {
                log.debug("User does not have the privilege to delete file: {}", id);
                throw new UnsupportedOperationException();
            }

            String uploadDir = env.getProperty(FILE_DIR_PROP);
            File targetFile = new File(uploadDir + File.separator + fileDescription.getName());

            if (targetFile.delete()) {
                fileDescription.getServices().remove(service);
                if (fileDescription.getServices().isEmpty()) {
                    fileDescriptionRepository.delete(fileDescription);
                } else {
                    fileDescriptionRepository.save(fileDescription);
                }
                log.info("File successfully deleted: {}, {}", id, service);
            } else {
                log.info("File could not be deleted: {}", id);
                log.debug("No file was found with the following id: {}", id);
                throw new FileNotFoundException();
            }
        } else {
            log.debug("No file was found with the following id: {}", id);
            throw new FileNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<FileDescription> getAllFilesByUsers(String userEmail) {
        List<FileDescription> fileDescriptionList = fileDescriptionRepository.findAllByUploadedBy(userEmail);
        Set<FileDescription> results = new HashSet<>(fileDescriptionList);
        log.info("Files found by file name: {}", userEmail);
        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<FileDescription> getAllFilesByServiceAndTagId(Long tagId, Service service) {
        List<FileDescription> fileDescriptions = fileDescriptionRepository.findAllByTagId(tagId);
        Set<FileDescription> results = new HashSet<>();
        for (FileDescription fd : fileDescriptions) {
            if (fd.getServices().contains(service)) {
                results.add(fd);
            }
        }
        return results;
    }

    /**
     * {@inheritDoc}
     */
    public File getFile(String name) {
        String uploadDir = env.getProperty(FILE_DIR_PROP);
        return new File(uploadDir + File.separator + name);
    }

}
