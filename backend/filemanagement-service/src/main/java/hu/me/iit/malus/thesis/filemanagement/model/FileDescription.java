package hu.me.iit.malus.thesis.filemanagement.model;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import hu.me.iit.malus.thesis.filemanagement.controller.dto.Service;
import lombok.*;

import javax.persistence.*;
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
    String downloadLink;
    long size;
    BlobId blobId;
    Date uploadDate;
    String uploadedBy;
    String contentType;
    @Enumerated(EnumType.STRING)
    @ElementCollection
    Set<Service> services;



}
