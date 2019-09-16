package hu.me.iit.malus.thesis.filemanagement.service;

import hu.me.iit.malus.thesis.filemanagement.controller.dto.Service;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescription;
import hu.me.iit.malus.thesis.filemanagement.model.exceptions.FileCouldNotBeUploaded;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Set;


/**
 * Interface of the FileDescription managment service
 * Should define all the possible operations for the service
 *
 * @author Ilku Krisztián
 */
public interface FileManagementService {

    FileDescription uploadFile(Part file, String services) throws FileCouldNotBeUploaded, IOException;
    FileDescription getById(Long id);
    Set<FileDescription> getAllByFileName(String filename);
    Set<FileDescription> getAllFiles();
    void deleteFile(Long id, Service service);
    Set<FileDescription> getAllFilesByServices(String services);
}
