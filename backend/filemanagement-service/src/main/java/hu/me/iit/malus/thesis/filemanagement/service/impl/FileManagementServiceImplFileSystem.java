package hu.me.iit.malus.thesis.filemanagement.service.impl;

import hu.me.iit.malus.thesis.filemanagement.controller.dto.FileDescriptorDto;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescriptor;
import hu.me.iit.malus.thesis.filemanagement.model.ServiceType;
import hu.me.iit.malus.thesis.filemanagement.repository.FileDescriptorRepository;
import hu.me.iit.malus.thesis.filemanagement.service.FileManagementService;
import hu.me.iit.malus.thesis.filemanagement.service.converters.Converter;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.FileNotFoundException;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.ForbiddenFileDeleteException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Local filesystem based implementation for the File management service.
 *
 * @author Attila Szőke
 **/
@Service
@Slf4j
@RequiredArgsConstructor
@Profile("!google")
public class FileManagementServiceImplFileSystem implements FileManagementService {

    private static final String FILE_DIR_PROP = "files.upload.dir";
    private static final String FILE_NAME_PATTERN = "%d_%d_%d_%s";
    private static final String DOWNLOAD_LINK_PATTERN = "/api/filemanagement/download/link/%s";

    private final Environment env; // environment is used for retrieving the upload destination from cloud props file, because @Value sets null
    private final FileDescriptorRepository fileDescriptorRepository;

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public FileDescriptorDto uploadFile(MultipartFile file, ServiceType serviceType, String userEmail, Long tagId) throws IOException {
        var fileName = String.format(
                FILE_NAME_PATTERN, userEmail.hashCode(), serviceType.hashCode(), tagId.hashCode(), UUID.randomUUID());
        var downloadLink = String.format(DOWNLOAD_LINK_PATTERN, fileName);
        var fileDescriptor = new FileDescriptor(
                null, fileName, downloadLink, file.getSize(), new Date(), userEmail, file.getContentType(), serviceType, tagId);
        String uploadDir = env.getProperty(FILE_DIR_PROP);
        var targetFile = Path.of(uploadDir, fileName);
        Files.createDirectories(targetFile.getParent());
        Files.copy(file.getInputStream(), targetFile);
        log.debug("File successfully uploaded: {}", fileName);
        fileDescriptor = fileDescriptorRepository.save(fileDescriptor);
        log.debug("File descriptor successfully saved to database: {}", fileDescriptor);
        return Converter.createFileDescriptorDtoFromFileDescriptor(fileDescriptor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFile(Long id, ServiceType serviceType, String email, String userRole)
            throws ForbiddenFileDeleteException, FileNotFoundException {
        var fileDescriptor = fileDescriptorRepository.findById(id).orElseThrow(() -> {
            log.debug("No file was found with the following id: {}", id);
            return new FileNotFoundException();
        });
        if (!(userRole.equals("ROLE_Teacher") || !fileDescriptor.getUploadedBy().equals(email))) {
            log.warn("User: {}, a(n) {} does not have the privilege to delete file {}", email, userRole, id);
            throw new ForbiddenFileDeleteException();
        }
        String uploadDir = env.getProperty(FILE_DIR_PROP);
        var targetFile = Path.of(uploadDir, fileDescriptor.getName());
        try {
            Files.delete(targetFile);
            fileDescriptorRepository.delete(fileDescriptor);
            log.debug("File successfully deleted: {}", id);
        } catch (IOException e) {
            log.error("File could not be deleted: {}", id);
            throw new FileNotFoundException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List<FileDescriptorDto> getAllFilesByUser(String userEmail) {
        List<FileDescriptor> results = fileDescriptorRepository.findAllByUploadedBy(userEmail);
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
        List<FileDescriptor> results = fileDescriptorRepository.findAllByServiceTypeAndTagId(serviceType, tagId);
        log.debug("Files found by file service {} and tagId {}: {}", serviceType, tagId, results);
        return Converter.createFileDescriptorDtoListFromFileDescriptorList(results);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public Path getFileByName(String name) {
        String uploadDir = env.getProperty(FILE_DIR_PROP);
        return Path.of(uploadDir, name);
    }

    @Override
    public void deleteFilesByServiceAndTagId(ServiceType serviceType, Long tagId, String email, String userRole)
            throws FileNotFoundException, UnsupportedOperationException, ForbiddenFileDeleteException {
        List<FileDescriptor> fileDescriptions = fileDescriptorRepository.findAllByServiceTypeAndTagId(serviceType, tagId);
        for (FileDescriptor fileDescription : fileDescriptions) {
            deleteFile(fileDescription.getId(), serviceType, email, userRole);
        }
    }
}
