package hu.me.iit.malus.thesis.filemanagement.controller.dto;

import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class File {

    String name;
    String downloadLink;
    long size;
    boolean canBeDeleted;
    Date uploadDate;
    String uploadedBy;
    String contentType;
    Set<Service> services;
}

