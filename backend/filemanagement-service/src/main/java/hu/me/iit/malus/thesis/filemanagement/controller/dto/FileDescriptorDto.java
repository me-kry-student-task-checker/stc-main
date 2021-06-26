package hu.me.iit.malus.thesis.filemanagement.controller.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class FileDescriptorDto {

    @Min(1)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String downloadLink;

    @NotNull
    private Date uploadDate;

    @NotBlank
    private String uploadedBy;

    @NotBlank
    private String contentType;

    @Min(1)
    private Long tagId;
}
