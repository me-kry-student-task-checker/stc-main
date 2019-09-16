package hu.me.iit.malus.thesis.filemanagement.service.impl;


import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescription;
import hu.me.iit.malus.thesis.filemanagement.model.exceptions.FileCouldNotBeUploaded;
import hu.me.iit.malus.thesis.filemanagement.repository.FileDescriptionRepository;
import hu.me.iit.malus.thesis.filemanagement.service.FileManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.Part;
import java.io.IOException;
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
    //private Bucket bucket;

    @Autowired
    public FileManagementServiceImpl(FileDescriptionRepository fileDescriptionRepository) {
        storage = StorageOptions.getDefaultInstance().getService();
        this.fileDescriptionRepository = fileDescriptionRepository;
        BucketInfo bucketInfo = BucketInfo.newBuilder(BUCKET_NAME).build();
        //this.bucket = storage.create(bucketInfo, Storage.BucketTargetOption.metagenerationMatch());
    }

    @Override
    public FileDescription uploadFile(Part file, String services) throws FileCouldNotBeUploaded, IOException {
        Blob blob = storage.create(BlobInfo.newBuilder(BUCKET_NAME, file.getSubmittedFileName()).build(), file.getInputStream());
        log.info("File successfully uploaded: {}", file.getSubmittedFileName());
        FileDescription fileDescription = new FileDescription();
        fileDescription.setUploadDate(new Date());
        fileDescription.setName(blob.getName());
        fileDescription.setSubmittedName(file.getSubmittedFileName());
        fileDescription.setDownloadLink(blob.getMediaLink());
        fileDescription.setSize(file.getSize());
        fileDescription.setBlobId(blob.getBlobId());
        fileDescription.setContentType(file.getContentType());
        if (fileDescription.getServices() == null) fileDescription.setServices(new HashSet<>());
        fileDescription.getServices().add(services);

        for(FileDescription fd : fileDescriptionRepository.findAll()){
            if (fd.getSubmittedName().equals(fileDescription.getSubmittedName())) {
                fileDescription.setId(fd.getId());
                fileDescription.getServices().addAll(fd.getServices());
                break;
            }
        }
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

        Iterable<FileDescription> fileDescriptionList = fileDescriptionRepository.findAllByName(filename);
        Set<FileDescription> results = new HashSet<>();
        for (FileDescription fd : fileDescriptionList) {
            results.add(fd);
        }
        log.info("Files found by file name: {}", filename);
        return results;
    }

    @Override
    public Set<FileDescription> getAllFiles() {
        Iterable<FileDescription> fileDescriptions = fileDescriptionRepository.findAll();
        Set<FileDescription> result = new HashSet<>();
        for (FileDescription i : fileDescriptions) {
            result.add(i);
        }
        /*Page<Blob> blobs = bucket.list();
        for (Blob blob : blobs.iterateAll()) {
            log.info("Ezeket talaltam: " + blob.toString());
        }*/
        return result;
    }

    //TODO: As the authentication is ready, check whether the user can delete the file
    @Override
    public void deleteFile(Long id) {
        boolean fileToBeRemovedIsPresent = fileDescriptionRepository.findById(id).isPresent();
        if (fileToBeRemovedIsPresent) {
            FileDescription fileToBeRemoved = fileDescriptionRepository.findById(id).get();
            BlobId blobId = BlobId.of(BUCKET_NAME, fileToBeRemoved.getName());
            log.info("blobID: " + fileToBeRemoved.getBlobId().toString());
            log.info("myBlobID: " + fileToBeRemoved.getBlobId().toString());
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

    @Override
    public Set<FileDescription> getAllFilesByServices(String service) {
        Set<FileDescription> files = new HashSet<>();
        for (FileDescription fd : fileDescriptionRepository.findAllByServices(service)) {
            files.add(fd);
        }
        return files;
    }
}
