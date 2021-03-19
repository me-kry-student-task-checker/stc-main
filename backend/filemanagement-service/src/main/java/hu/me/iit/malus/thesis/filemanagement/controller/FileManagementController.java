package hu.me.iit.malus.thesis.filemanagement.controller;


import hu.me.iit.malus.thesis.filemanagement.controller.converters.Converter;
import hu.me.iit.malus.thesis.filemanagement.controller.dto.FileDescriptorDto;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescription;
import hu.me.iit.malus.thesis.filemanagement.model.Service;
import hu.me.iit.malus.thesis.filemanagement.service.FileManagementService;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.FileNotFoundException;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.UnsupportedOperationException;
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
    public ResponseEntity<@Size(min = 1) Set<@Valid FileDescriptorDto>> uploadFiles(@FormDataParam("file") @Size(min = 1) List<Part> file,
                                                                                    @FormParam("service") @NotNull Service service,
                                                                                    @FormParam("tagId") @Min(1) Long tagId,
                                                                                    Principal principal) throws IOException {
        Set<FileDescriptorDto> result = new HashSet<>();
        for (Part f : file) {
            FileDescription fd = fileManagementService.uploadFile(f, service, principal.getName(), tagId);
            result.add(Converter.FileDescriptionToFile(fd));
        }

        return ResponseEntity
                .status(200)
                .body(result);
    }


    @DeleteMapping("/delete/{id}/{service}")
    public ResponseEntity<String> deleteFile(@PathVariable @Min(1) Long id, @PathVariable @NotNull Service service, Principal principal) {
        // TODO controller advice try catch helyett mindenhol a controllerben
        try {
            fileManagementService.deleteFile(id, service, principal.getName());
        } catch (UnsupportedOperationException e) {
            return ResponseEntity
                    .status(403)
                    .body("User does not have the privilege to delete this file!");
        } catch (FileNotFoundException ex) {
            return ResponseEntity
                    .status(404)
                    .body("File not found!");
        }
        return ResponseEntity
                .status(200)
                .body("Successful delete!");
    }

    @GetMapping("/download/getByUser/{userEmail}")
    public ResponseEntity<Set<@Valid FileDescriptorDto>> getFilesByUser(@PathVariable @NotBlank String userEmail) {
        // TODO user from principal
        Set<FileDescriptorDto> fileDescriptorDtos = new HashSet<>();
        fileManagementService.getAllFilesByUser(userEmail).forEach(fd -> fileDescriptorDtos.add(Converter.FileDescriptionToFile(fd)));
        return ResponseEntity
                .status(200)
                .body(fileDescriptorDtos);
    }

    @GetMapping("/download/getByTagId/{service}/{tagId}")
    public ResponseEntity<Set<@Valid FileDescriptorDto>> getFilesByTagIdAndService(@PathVariable @NotNull Service service,
                                                                                   @PathVariable @Min(1) Long tagId) {
        Set<FileDescriptorDto> results = new HashSet<>();
        fileManagementService.getAllFilesByServiceAndTagId(tagId, service).forEach(fileDescription -> results.add(Converter.FileDescriptionToFile(fileDescription)));
        return ResponseEntity
                .status(200)
                .body(results);
    }

    @GetMapping("/download/link/{name}")
    public ResponseEntity<FileSystemResource> getFileByName(@PathVariable @NotBlank String name) {
        return ResponseEntity.ok(new FileSystemResource(fileManagementService.getFileByName(name)));
    }
}
