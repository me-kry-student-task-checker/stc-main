package hu.me.iit.malus.thesis.filemanagement.controller.dto;

import lombok.*;

import java.util.Date;

@Getter @Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class File {

    String name;
    String submittedName;
    String downloadLink;
    long size;
    Date uploadDate;

}