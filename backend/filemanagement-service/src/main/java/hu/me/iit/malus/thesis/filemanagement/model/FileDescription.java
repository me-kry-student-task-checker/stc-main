package hu.me.iit.malus.thesis.filemanagement.model;

import hu.me.iit.malus.thesis.filemanagement.controller.dto.Service;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
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
    @Column(length = 2000)
    String name;
    @Column(length = 5000)
    String downloadLink;
    long size;
    Date uploadDate;
    String uploadedBy;
    String contentType;
    @Enumerated(EnumType.STRING)
    @ElementCollection
    Set<Service> services;
    Long tagId;


}
