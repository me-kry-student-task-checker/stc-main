package hu.me.iit.malus.thesis.filemanagement.service.impl;

import hu.me.iit.malus.thesis.filemanagement.controller.dto.FileDescriptorDto;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescriptor;
import hu.me.iit.malus.thesis.filemanagement.model.Service;
import hu.me.iit.malus.thesis.filemanagement.repository.FileDescriptionRepository;
import hu.me.iit.malus.thesis.filemanagement.service.FileManagementService;
import hu.me.iit.malus.thesis.filemanagement.service.converters.Converter;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.FileNotFoundException;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.ForbiddenFileDeleteException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     *
     * @return
     */
    @Override
    public FileDescriptorDto uploadFile(Part file, Service service, String user, Long tagId) throws IOException {
        String fileName = user.hashCode() + "_" + service.hashCode() + "_" + tagId.hashCode() + "_" + file.getSubmittedFileName();
        FileDescriptor fileDescriptor = new FileDescriptor();
        fileDescriptor.setUploadDate(new Date());
        fileDescriptor.setName(fileName);
        fileDescriptor.setDownloadLink("/api/filemanagement/download/link/" + fileName);
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

        String uploadDir = env.getProperty(FILE_DIR_PROP);
        File targetFile = new File(uploadDir + File.separator + fileName);
        FileUtils.copyInputStreamToFile(file.getInputStream(), targetFile);
        log.debug("File successfully uploaded: {}", file.getSubmittedFileName());

        fileDescriptionRepository.save(fileDescriptor);
        log.debug("File description successfully saved to database: {}", fileDescriptor);

        return Converter.createFileDescriptorDtoFromFileDescriptor(fileDescriptor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFile(Long id, Service service, String username) throws ForbiddenFileDeleteException, FileNotFoundException {
        FileDescriptor fileDescriptor = fileDescriptionRepository.findById(id).orElseThrow(() -> {
            log.debug("No file was found with the following id: {}", id);
            return new FileNotFoundException();
        });
        if (!fileDescriptor.getUploadedBy().equalsIgnoreCase(username)) {
            log.warn("User does not have the privilege to delete file: {}", id);
            throw new ForbiddenFileDeleteException();
        }

        String uploadDir = env.getProperty(FILE_DIR_PROP);
        File targetFile = new File(uploadDir + File.separator + fileDescriptor.getName());

        boolean deleteSuccessful = targetFile.delete();
        if (deleteSuccessful) {
            fileDescriptor.getServices().remove(service);
            if (fileDescriptor.getServices().isEmpty()) {
                fileDescriptionRepository.delete(fileDescriptor);
            } else {
                fileDescriptionRepository.save(fileDescriptor);
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
        List<FileDescriptor> fileDescriptorList = fileDescriptionRepository.findAllByUploadedBy(userEmail);
        Set<FileDescriptor> results = new HashSet<>(fileDescriptorList);
        log.debug("Files found by user {}: {}", userEmail, results);
        return Converter.createFileDescriptorDtosFromFileDescriptors(results);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public Set<FileDescriptorDto> getAllFilesByServiceAndTagId(Long tagId, Service service) {
        List<FileDescriptor> fileDescriptors = fileDescriptionRepository.findAllByTagId(tagId);
        Set<FileDescriptor> results = new HashSet<>();
        for (FileDescriptor fd : fileDescriptors) {
            if (fd.getServices().contains(service)) {
                results.add(fd);
            }
        }
        log.debug("Files found by file service {} and tagId {}: {}", service, tagId, results);
        return Converter.createFileDescriptorDtosFromFileDescriptors(results);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getFileByName(String name) {
        String uploadDir = env.getProperty(FILE_DIR_PROP);
        return new File(uploadDir + File.separator + name);
    }

    @Override
    public void deleteFilesByServiceAndTagId(Service service, Long tagId, String email, String userRole)
            throws FileNotFoundException, UnsupportedOperationException {
        List<FileDescription> fileDescriptions = fileDescriptionRepository.findAllByServicesContainingAndTagId(service, tagId);
        for (FileDescription fileDescription : fileDescriptions) {
            deleteFile(fileDescription.getId(), service, email, userRole);
        }
    }
}
