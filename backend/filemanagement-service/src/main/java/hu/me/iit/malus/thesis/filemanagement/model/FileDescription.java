package hu.me.iit.malus.thesis.filemanagement.model;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import hu.me.iit.malus.thesis.filemanagement.controller.dto.Service;
import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Defines a File's Description
 * @author Ilku Krisztian
 */

@Entity
@Getter @Setter
@NoArgsConstructor @ToString
@EqualsAndHashCode
public class FileDescription {

    @GeneratedValue
    @Id
    Long id;
    String name;
    String submittedName;
    String downloadLink;
    long size;
    BlobId blobId;
    Date uploadDate;
    String uploadedBy;
    String contentType;
    @ElementCollection
    Set<String> services;



}
