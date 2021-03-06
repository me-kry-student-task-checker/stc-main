package hu.me.iit.malus.thesis.filemanagement.controller;


import hu.me.iit.malus.thesis.filemanagement.controller.converters.Converter;
import hu.me.iit.malus.thesis.filemanagement.controller.dto.File;
import hu.me.iit.malus.thesis.filemanagement.controller.dto.Service;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescription;
import hu.me.iit.malus.thesis.filemanagement.service.FileManagementService;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.FileNotFoundException;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.UnsupportedOperationException;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Part;
import javax.ws.rs.FormParam;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Controller endpoint of this service, which handles file transfers
 * @author Ilku Krisztián
 */
@RestController
@RequestMapping("/api/filemanagement")
public class FileManagementController {

    private FileManagementService fileManagementService;

    @Autowired
    public FileManagementController(FileManagementService fileManagementService) {
        this.fileManagementService = fileManagementService;
    }

    @PostMapping("/upload")
    public ResponseEntity<File> uploadFile(@FormDataParam("file") Part file, @FormParam("service") Service service, @FormParam("tagId") Long tagId, Principal principal) throws IOException {
        FileDescription fd = fileManagementService.uploadFile(file, service, principal.getName(), tagId);
        if (fd == null) return ResponseEntity
                .status(204)
                .body(null);

        return ResponseEntity
                .status(200)
                .body(Converter.FileDescriptionToFile(fd));
    }

    @PostMapping("/uploadFiles")
    public ResponseEntity<Set<File>> uploadFileMultipleFiles(@FormDataParam("file") ArrayList<Part> file, @FormParam("service") Service service, @FormParam("tagId") Long tagId, Principal principal) throws IOException {
        Set<File> result = new HashSet<>();
        for (Part f : file) {
            FileDescription fd = fileManagementService.uploadFile(f, service, principal.getName(), tagId);
            result.add(Converter.FileDescriptionToFile(fd));
        }

        return ResponseEntity
                .status(200)
                .body(result);
    }


    @DeleteMapping("/delete/{id}/{service}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id, @PathVariable Service service, Principal principal) {
        try {
            fileManagementService.deleteFile(id, service, principal.getName());
        }catch(UnsupportedOperationException e) {
            return ResponseEntity
                    .status(403)
                    .body("User does not have the privilege to delete this file!");
        }catch (FileNotFoundException ex) {
            return ResponseEntity
                    .status(404)
                    .body("File not found!");
        }
        return ResponseEntity
                .status(200)
                .body("Successful delete!");
    }

    @GetMapping("/download/getById/{id}")
    public ResponseEntity<File> getFileById(@PathVariable Long id){
        FileDescription fd = fileManagementService.getById(id);
        if(fd == null) {
            return ResponseEntity
                    .status(204)
                    .body(null);
        }
        return ResponseEntity
                .status(200)
                .body(Converter.FileDescriptionToFile(fd));
    }

    @GetMapping("/download/getByFilename/{filename}")
    public ResponseEntity<Set<File>> getFileByFileName(@PathVariable String filename){
        Set<File> files = new HashSet<>();
        fileManagementService.getAllByFileName(filename).forEach(fd -> files.add(Converter.FileDescriptionToFile(fd)));
        return ResponseEntity
                .status(200)
                .body(files);
    }

    @GetMapping("/download/getByUser/{user}")
    public ResponseEntity<Set<File>> getFileByFileUser(@PathVariable String user){
        Set<File> files = new HashSet<>();
        fileManagementService.getAllFilesByUsers(user).forEach(fd -> files.add(Converter.FileDescriptionToFile(fd)));
        return ResponseEntity
                .status(200)
                .body(files);
    }

    @GetMapping("/download/getByService/{service}")
    public ResponseEntity<Set<File>> getAllFilesByService(@PathVariable Service service) {
        Set<FileDescription> files = new HashSet<>(fileManagementService.getAllFilesByServices(service));
        return ResponseEntity
                .status(200)
                .body(new HashSet<>(Converter.FileDescriptionsToFiles(files)));
    }

    @GetMapping("/download/getAll")
    public ResponseEntity<Set<File>> getAllFiles() {
        Set<File> files = new HashSet<>();
        fileManagementService.getAllFiles().forEach(fileDescription -> files.add(Converter.FileDescriptionToFile(fileDescription)));
        return ResponseEntity
                .status(200)
                .body(files);
    }

    @GetMapping("/download/getByTagId/{service}/{tagId}")
    public ResponseEntity<Set<File>> getAllFilesByTagId(@PathVariable Service service, @PathVariable Long tagId) {
        Set<File> results = new HashSet<>();
        fileManagementService.getAllFilesByTagId(tagId, service).forEach(fileDescription -> results.add(Converter.FileDescriptionToFile(fileDescription)));
        return ResponseEntity
                .status(200)
                .body(results);
    }

    @GetMapping("/download/link/{name}")
    public ResponseEntity<FileSystemResource> getByLink(@PathVariable String name) {
        return ResponseEntity.ok(new FileSystemResource(fileManagementService.getFile(name)));
    }
}
