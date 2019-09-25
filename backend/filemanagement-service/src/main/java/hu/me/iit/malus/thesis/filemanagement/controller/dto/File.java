package hu.me.iit.malus.thesis.filemanagement.controller.dto;

import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class File {

    Long id;
    String name;
    String downloadLink;
    Date uploadDate;
    String uploadedBy;
    Long tagId;
}
