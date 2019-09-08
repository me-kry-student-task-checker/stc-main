package hu.me.iit.malus.thesis.filemanagement.service;

import hu.me.iit.malus.thesis.filemanagement.model.FileDescription;
import hu.me.iit.malus.thesis.filemanagement.model.exceptions.FileCouldNotBeUploaded;

import java.io.InputStream;
import java.util.Set;


/**
 * Interface of the FileDescription managment service
 * Should define all the possible operations for the service
 *
 * @author Ilku Krisztián
 */
public interface FileManagementService {

    FileDescription uploadFile(String name, String submittedFileName, long size, InputStream inputStream) throws FileCouldNotBeUploaded;
    FileDescription getFileById(Long id);
    Set<FileDescription> getFileByFileName(String filename);
    Set<FileDescription> getAllFiles();
    void deleteFile(Long id);
}
