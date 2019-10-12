package hu.me.iit.malus.thesis.filemanagement.service;

import hu.me.iit.malus.thesis.filemanagement.controller.dto.Service;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescription;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.FileNotFoundException;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.UnsupportedOperationException;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Set;


/**
 * Interface of the File Management service
 * Should define all the possible operations for the service
 *
 * @author Ilku Krisztián
 */
public interface FileManagementService {

    /**
     * Uploads a file to Cloud Storage
     * @param file The file to be uploaded
     * @param service It defines the service, which the file has been uploaded from
     * @param user The email address of the user, who uploaded the file
     * @return The object that represents the file which was uploaded and saved to database.
     * @throws IOException
     */
    FileDescription uploadFile(Part file, Service service, String user, Long tagId) throws IOException;

    /**
     * Queries the files by their IDs
     * @param id The parameter that filters the files
     * @return If it founds by the parameter than returns the value, else return null
     */
    FileDescription getById(Long id);

    /**
     * Queries the files by their name
     * @param filename The parameter that filters the files
     * @return If it founds by the parameter than returns the value, else return empty Set
     */
    Set<FileDescription> getAllByFileName(String filename);

    /**
     * Query all files, that can be found in the DB
     * @return All files found in DB, empty Set if DB is empty
     */
    Set<FileDescription> getAllFiles();

    /**
     *  Deletes a file from the Cloud Storage Bucket. If the file is uploaded by multiple services, than it just removes the entry from the services field.
     *  If the last service entry is removed from list, than it will be removed from the database as well
     * @param id The ID of the file
     * @param service The service that uploaded the file
     */
    void deleteFile(Long id, Service service, String username) throws UnsupportedOperationException, FileNotFoundException;

    /**
     * Queries all files by the services that uploaded them
     * @param services The parameter that filters the files
     * @return If it founds by the parameter than returns the value, else return empty Set
     */
    Set<FileDescription> getAllFilesByServices(Service services);

    /**
     * Queries all files by the services that uploaded them
     * @param userEmail The parameter that filters the files
     * @return If it founds by the parameter than returns the value, else return empty Set
     */
    Set<FileDescription> getAllFilesByUsers(String userEmail);

    /**
     * Queries all files based on the id set and given outside of fileservice filtered by service
     * @param tagId - The id, given when a file is sent in
     * @param service - The service which sent the file in
     * @return Filtered Set of files based on the given parameters
     */
    Set<FileDescription> getAllFilesByTagId(Long tagId, Service service);
}
