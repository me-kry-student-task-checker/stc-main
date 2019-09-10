package hu.me.iit.malus.thesis.filemanagement.service.impl;


import com.google.cloud.storage.*;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescription;
import hu.me.iit.malus.thesis.filemanagement.model.exceptions.FileCouldNotBeUploaded;
import hu.me.iit.malus.thesis.filemanagement.repository.FileDescriptionRepository;
import hu.me.iit.malus.thesis.filemanagement.service.FileManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

    @Override
    public FileDescription uploadFile(String fileName, String submittedFileName, long size, InputStream content) throws FileCouldNotBeUploaded {
        List<Acl> acls = new ArrayList<>();
        acls.add(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        Blob blob = storage.create(BlobInfo.newBuilder(BUCKET_NAME, fileName).setAcl(acls).build(), content);

        log.info("File successfully uploaded: {}", fileName);

        FileDescription fileDescription = new FileDescription();
        fileDescription.setUploadDate(new Date());
        fileDescription.setName(blob.getName());
        fileDescription.setSubmittedName(submittedFileName);
        fileDescription.setDownloadLink(blob.getMediaLink());
        fileDescription.setSize(size);
        fileDescription.setBlobInfo(blob);

        fileDescriptionRepository.save(fileDescription);

        log.info("File description successfully saved to database: {}", fileDescription);

        return fileDescription;
    }

    @Override
    public FileDescription getFileById(Long id) {
        boolean isPresent = fileDescriptionRepository.findById(id).isPresent();
        if (isPresent) {
            log.info("File found: {}", id);
            return fileDescriptionRepository.findById(id).get();
        }

        log.info("File could not be found: {}", id);
        return new FileDescription();
    }

    @Override
    public Set<FileDescription> getFileByFileName(String filename) {
        log.info("Files found by file name: {}", filename);
        return (Set<FileDescription>) fileDescriptionRepository.findAllByName(filename);
    }

    @Override
    public Set<FileDescription> getAllFiles() {
        return (Set<FileDescription>) fileDescriptionRepository.findAll();
    }

    @Override
    public void deleteFile(Long id) {
        boolean fileToBeRemovedIsPresent = fileDescriptionRepository.findById(id).isPresent();
        if (fileToBeRemovedIsPresent) {
            FileDescription fileToBeRemoved = fileDescriptionRepository.findById(id).get();
            BlobId blobId = BlobId.of(BUCKET_NAME, fileToBeRemoved.getBlobInfo().getName());
            boolean successful_delete = storage.delete(blobId);
            if (successful_delete) {
                fileDescriptionRepository.delete(fileToBeRemoved);
                log.info("File successfully deleted: {}", id);
            }
            else{
                log.info("File could not be deleted: {}", id);
                log.debug("No file was found with the following id: {}", id);
            }
        }
    }
}
