package hu.me.iit.malus.thesis.filemanagement.controller.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class FileDescriptorDto {

    @Min(1)
    Long id;

    @NotBlank
    String name;

    @NotBlank
    String downloadLink;

    @NotNull
    Date uploadDate;

    @NotBlank
    String uploadedBy;

    @NotBlank
    String contentType;

    @Min(1)
    Long tagId;
}
