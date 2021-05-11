package hu.me.iit.malus.thesis.filemanagement.controller;


import hu.me.iit.malus.thesis.filemanagement.controller.converters.Converter;
import hu.me.iit.malus.thesis.filemanagement.controller.dto.FileDescriptorDto;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescription;
import hu.me.iit.malus.thesis.filemanagement.model.Service;
import hu.me.iit.malus.thesis.filemanagement.service.FileManagementService;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.FileNotFoundException;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.ForbiddenFileDeleteException;
import lombok.RequiredArgsConstructor;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Part;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;
import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controller endpoint of this service, which handles file transfers
 *
 * @author Ilku Krisztián
 * @author Attila Szőke
 */
@RestController
@RequestMapping("/api/filemanagement")
@RequiredArgsConstructor
public class FileManagementController {

    private final FileManagementService fileManagementService;

    @PostMapping("/uploadFiles")
    public ResponseEntity<@Size(min = 1) Set<@Valid FileDescriptorDto>> uploadFiles(
            @FormDataParam("file") @Size(min = 1) List<Part> file,
            @FormParam("service") @NotNull Service service,
            @FormParam("tagId") @Min(1) Long tagId,
            Principal principal
    ) throws IOException {
        Set<FileDescriptorDto> result = new HashSet<>();
        for (Part f : file) {
            FileDescription fd = fileManagementService.uploadFile(f, service, principal.getName(), tagId);
            result.add(Converter.FileDescriptionToFile(fd));
        }
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/delete/{id}/{service}")
    public ResponseEntity<Void> deleteFile(@PathVariable @Min(1) Long id, @PathVariable @NotNull Service service, Principal principal)
            throws ForbiddenFileDeleteException, FileNotFoundException {
        fileManagementService.deleteFile(id, service, principal.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/download/getByUser")
    public ResponseEntity<Set<@Valid FileDescriptorDto>> getFilesByUser(Principal principal) {
        Set<FileDescriptorDto> fileDescriptorDtoList = new HashSet<>();
        fileManagementService.getAllFilesByUser(principal.getName()).forEach(fd -> fileDescriptorDtoList.add(Converter.FileDescriptionToFile(fd)));
        return ResponseEntity.ok(fileDescriptorDtoList);
    }

    @GetMapping("/download/getByTagId/{service}/{tagId}")
    public ResponseEntity<Set<@Valid FileDescriptorDto>> getFilesByTagIdAndService(
            @PathVariable @NotNull Service service,
            @PathVariable @Min(1) Long tagId) {
        Set<FileDescriptorDto> results = new HashSet<>();
        fileManagementService.getAllFilesByServiceAndTagId(tagId, service).forEach(
                fileDescription -> results.add(Converter.FileDescriptionToFile(fileDescription)));
        return ResponseEntity.ok(results);
    }

    @GetMapping("/download/link/{name}")
    public ResponseEntity<FileSystemResource> getFileByName(@PathVariable @NotBlank String name) {
        return ResponseEntity.ok(new FileSystemResource(fileManagementService.getFileByName(name)));
    }
}
