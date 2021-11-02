package hu.me.iit.malus.thesis.filemanagement.controller;


import hu.me.iit.malus.thesis.filemanagement.controller.dto.FileDescriptorDto;
import hu.me.iit.malus.thesis.filemanagement.controller.dto.FileUploadDto;
import hu.me.iit.malus.thesis.filemanagement.model.ServiceType;
import hu.me.iit.malus.thesis.filemanagement.service.FileManagementService;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.FileNotFoundException;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.ForbiddenFileDeleteException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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

    @PostMapping(value = "/uploadFiles", consumes = "multipart/form-data")
    public ResponseEntity<@Size(min = 1) List<@Valid FileDescriptorDto>> uploadFiles(@Valid FileUploadDto dto, Principal principal)
            throws IOException {
        List<FileDescriptorDto> result = new ArrayList<>();
        for (MultipartFile file : dto.getFiles()) {
            result.add(fileManagementService.uploadFile(file, dto.getServiceType(), principal.getName(), dto.getTagId()));
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{id}/{serviceType}")
    public ResponseEntity<Void> deleteFile(@PathVariable @Min(1) Long id, @PathVariable @NotNull ServiceType serviceType, Authentication auth)
            throws ForbiddenFileDeleteException, FileNotFoundException {
        fileManagementService.deleteFile(id, serviceType, auth.getName(), auth.getAuthorities().stream().findFirst().get().getAuthority());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteAll/{serviceType}/{tagId}")
    public void removeFilesByServiceAndTagId(@PathVariable ServiceType serviceType, @PathVariable Long tagId, Authentication authentication)
            throws FileNotFoundException, UnsupportedOperationException, ForbiddenFileDeleteException {
        fileManagementService.deleteFilesByServiceAndTagId(
                serviceType, tagId, authentication.getName(), authentication.getAuthorities().stream().findFirst().get().getAuthority());
    }

    @GetMapping("/download/getByUser")
    public ResponseEntity<List<@Valid FileDescriptorDto>> getFilesByUser(Principal principal) {
        return ResponseEntity.ok(fileManagementService.getAllFilesByUser(principal.getName()));
    }

    @GetMapping("/download/getByTagId/{serviceType}/{tagId}")
    public ResponseEntity<List<@Valid FileDescriptorDto>> getFilesByTagIdAndService(
            @PathVariable @NotNull ServiceType serviceType,
            @PathVariable @Min(1) Long tagId) {
        return ResponseEntity.ok(fileManagementService.getAllFilesByServiceTypeAndTagId(tagId, serviceType));
    }

    @GetMapping("/download/link/{name}")
    public ResponseEntity<FileSystemResource> getFileByName(@PathVariable @NotBlank String name) {
        return ResponseEntity.ok(new FileSystemResource(fileManagementService.getFileByName(name)));
    }

    @PostMapping("/prepare/remove/by/service/{serviceType}/and/{tagIds}")
    public String prepareRemoveFilesByServiceTypeAndTagIds(@PathVariable ServiceType serviceType, @PathVariable List<Long> tagIds) {
        return fileManagementService.prepareRemoveFilesByServiceAndTagId(serviceType, tagIds);
    }

    @PostMapping("/commit/remove/{transactionKey}")
    public void commitRemoveFilesByServiceTypeAndTagIds(@PathVariable String transactionKey) {
        fileManagementService.commitRemoveFilesByServiceAndTagId(transactionKey);
    }

    @PostMapping("/rollback/remove/{transactionKey}")
    public void rollbackRemoveFilesByServiceTypeAndTagIds(@PathVariable String transactionKey) {
        fileManagementService.rollbackRemoveFilesByServiceAndTagId(transactionKey);
    }

}
