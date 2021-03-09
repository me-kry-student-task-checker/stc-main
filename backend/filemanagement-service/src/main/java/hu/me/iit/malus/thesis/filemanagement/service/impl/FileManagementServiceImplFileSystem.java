package hu.me.iit.malus.thesis.filemanagement.service.impl;

import com.google.api.client.util.Value;
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

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@org.springframework.stereotype.Service
@Slf4j
@RequiredArgsConstructor
@Profile("!google")
public class FileManagementServiceImplFileSystem implements FileManagementService {

    @Value("${files.uploadDir}")
    private String uploadDir;
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
    public FileDescription getById(Long id) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<FileDescription> getAllByFileName(String filename) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<FileDescription> getAllFiles() {
        return new HashSet<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFile(Long id, Service service, String username) throws UnsupportedOperationException, FileNotFoundException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<FileDescription> getAllFilesByServices(Service services) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<FileDescription> getAllFilesByUsers(String userEmail) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<FileDescription> getAllFilesByTagId(Long tagId, Service service) {
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
        return new File(uploadDir + File.separator + name);
    }

}
