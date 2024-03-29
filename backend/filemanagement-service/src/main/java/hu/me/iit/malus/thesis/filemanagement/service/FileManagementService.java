package hu.me.iit.malus.thesis.filemanagement.service;

import hu.me.iit.malus.thesis.filemanagement.controller.dto.FileDescriptorDto;
import hu.me.iit.malus.thesis.filemanagement.model.ServiceType;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.FileNotFoundException;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.ForbiddenFileDeleteException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;


/**
 * Interface of the File Management service
 * Should define all the possible operations for the service
 *
 * @author Ilku Krisztián
 * @author Attila Szőke
 */
public interface FileManagementService {

    /**
     * Uploads a file to the storage Storage.
     *
     * @param file        The file to be uploaded
     * @param serviceType It defines the service, which the file has been uploaded from
     * @param userEmail   The email address of the userEmail, who uploaded the file
     * @param tagId       the tag id
     * @return The object that represents the file which was uploaded and saved to database.
     * @throws IOException thrown when the file saving fails
     */
    FileDescriptorDto uploadFile(MultipartFile file, ServiceType serviceType, String userEmail, Long tagId) throws IOException;

    /**
     * Deletes a file from the storage. If the file is uploaded by multiple services, than it just removes the entry from the services field.
     * If the last service entry is removed from list, than it will be removed from the database as well.
     *
     * @param id          The ID of the file
     * @param serviceType The service that uploaded the file
     * @param username    the username
     * @param userRole    the user role
     * @throws ForbiddenFileDeleteException the forbidden file delete exception
     * @throws FileNotFoundException        the file not found exception
     */
    void deleteFile(Long id, ServiceType serviceType, String username, String userRole) throws ForbiddenFileDeleteException, FileNotFoundException;

    /**
     * Delete files by service and tag id.
     *
     * @param serviceType the service type
     * @param tagId       the tag id
     * @param name        the name
     * @param authority   the authority
     * @throws FileNotFoundException        the file not found exception
     * @throws ForbiddenFileDeleteException the forbidden file delete exception
     */
    void deleteFilesByServiceAndTagId(ServiceType serviceType, Long tagId, String name, String authority) throws FileNotFoundException, ForbiddenFileDeleteException;

    /**
     * Queries all uploaded files of a user.
     *
     * @param userEmail The parameter that filters the files
     * @return If it founds by the parameter than returns the value, else return empty Set
     */
    List<FileDescriptorDto> getAllFilesByUser(String userEmail);

    /**
     * Queries all files based on the Id and the Service it belongs to.
     *
     * @param tagId       - The id, given when a file is sent in
     * @param serviceType - The service which sent the file in
     * @return Filtered Set of files based on the given parameters
     */
    List<FileDescriptorDto> getAllFilesByServiceTypeAndTagId(Long tagId, ServiceType serviceType);

    /**
     * Returns a file;
     *
     * @param name name of the file
     * @return the file
     */
    Path getFileByName(String name);

    /**
     * 2PC prepare phase, prepares to delete files based on service and tag ids.
     *
     * @param serviceType the service type
     * @param tagIds      the tag id list
     * @return the transaction key
     */
    String prepareRemoveFilesByServiceAndTagId(ServiceType serviceType, List<Long> tagIds);

    /**
     * 2PC commit phase, commits delete files operation based on service and tag id.
     *
     * @param transactionKey the transaction key
     */
    void commitRemoveFilesByServiceAndTagId(String transactionKey);

    /**
     * 2PC rollback phase, rolls back delete files operation based on service and tag id.
     *
     * @param transactionKey the transaction key
     */
    void rollbackRemoveFilesByServiceAndTagId(String transactionKey);
}
