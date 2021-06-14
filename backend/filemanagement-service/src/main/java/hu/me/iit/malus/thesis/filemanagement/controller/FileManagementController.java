package hu.me.iit.malus.thesis.filemanagement.controller;


import hu.me.iit.malus.thesis.filemanagement.controller.dto.FileDescriptorDto;
import hu.me.iit.malus.thesis.filemanagement.model.ServiceType;
import hu.me.iit.malus.thesis.filemanagement.service.FileManagementService;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.FileNotFoundException;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.ForbiddenFileDeleteException;
import lombok.RequiredArgsConstructor;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
            @FormParam("service") @NotNull ServiceType serviceType,
            @FormParam("tagId") @Min(1) Long tagId,
            Principal principal
    ) throws IOException {
        Set<FileDescriptorDto> result = new HashSet<>();
        for (Part f : file) {
            FileDescriptorDto fd = fileManagementService.uploadFile(f, serviceType, principal.getName(), tagId);
            result.add(fd);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{id}/{serviceType}")
    public ResponseEntity<Void> deleteFile(@PathVariable @Min(1) Long id, @PathVariable @NotNull ServiceType serviceType, Authentication auth)
            throws ForbiddenFileDeleteException, FileNotFoundException {
        fileManagementService.deleteFile(id, serviceType, auth.getName(), auth.getAuthorities().stream().findFirst().get().getAuthority());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/download/getByUser")
    public ResponseEntity<Set<@Valid FileDescriptorDto>> getFilesByUser(Principal principal) {
        return ResponseEntity.ok(fileManagementService.getAllFilesByUser(principal.getName()));
    }

    @GetMapping("/download/getByTagId/{serviceType}/{tagId}")
    public ResponseEntity<Set<@Valid FileDescriptorDto>> getFilesByTagIdAndService(
            @PathVariable @NotNull ServiceType serviceType,
            @PathVariable @Min(1) Long tagId) {
        return ResponseEntity.ok(fileManagementService.getAllFilesByServiceAndTagId(tagId, serviceType));
    }

    @GetMapping("/download/link/{name}")
    public ResponseEntity<FileSystemResource> getFileByName(@PathVariable @NotBlank String name) {
        return ResponseEntity.ok(new FileSystemResource(fileManagementService.getFileByName(name)));
    }

    @DeleteMapping("/deleteAll/{serviceType}/{tagId}")
    public void removeFilesByServiceAndTagId(@PathVariable ServiceType serviceType, @PathVariable Long tagId, Authentication authentication)
            throws FileNotFoundException, UnsupportedOperationException, ForbiddenFileDeleteException {
        fileManagementService.deleteFilesByServiceAndTagId(
                serviceType, tagId, authentication.getName(), authentication.getAuthorities().stream().findFirst().get().getAuthority());
    }

}
