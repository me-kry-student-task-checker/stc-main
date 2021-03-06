package hu.me.iit.malus.thesis.filemanagement.controller.dto;

import hu.me.iit.malus.thesis.filemanagement.model.ServiceType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class FileUploadDto {

    @Size(min = 1)
    private List<MultipartFile> files;

    @NotNull
    private ServiceType serviceType;

    @Min(1)
    private Long tagId;
}
