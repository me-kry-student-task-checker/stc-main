package hu.me.iit.malus.thesis.filemanagement.model;

import com.google.cloud.storage.Blob;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

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
    Blob blobInfo;
    Date uploadDate;

}
