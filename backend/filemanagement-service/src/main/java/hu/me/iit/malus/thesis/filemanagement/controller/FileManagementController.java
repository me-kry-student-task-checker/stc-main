package hu.me.iit.malus.thesis.filemanagement.controller;


import hu.me.iit.malus.thesis.filemanagement.controller.converters.Converter;
import hu.me.iit.malus.thesis.filemanagement.controller.dto.File;
import hu.me.iit.malus.thesis.filemanagement.controller.dto.Service;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescription;
import hu.me.iit.malus.thesis.filemanagement.model.exceptions.FileCouldNotBeUploaded;
import hu.me.iit.malus.thesis.filemanagement.service.FileManagementService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Part;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

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
    public File uploadFile(@FormDataParam("file") Part file, @FormParam("service") Service service) throws FileCouldNotBeUploaded, IOException {
        //List<String> servicesAsStrings = service.stream().map(Enum::toString).collect(Collectors.toList());
        FileDescription fd = fileManagementService.uploadFile(file, service.toString());
        return Converter.FileDescriptionToFile(fd);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
    public void deleteFile(@PathVariable Long id) {
        fileManagementService.deleteFile(id);
    }

    @GetMapping("/download/getById/{id}")
    public File getFileById(@PathVariable Long id){
        FileDescription fd = fileManagementService.getFileById(id);
        return Converter.FileDescriptionToFile(fd);
    }

    @GetMapping("/download/getByFilename/{filename}")
    public Set<File> getFileByFileName(@PathVariable String filename){
        Set<File> files = new HashSet<>();

        fileManagementService.getFileByFileName(filename).stream().forEach(fd -> {
            files.add(Converter.FileDescriptionToFile(fd));
        });

        return files;
    }

    @GetMapping("/download/getall")
    public Set<File> getAllFiles() {
        Set<File> files = new HashSet<>();
        fileManagementService.getAllFiles().stream().forEach(fileDescription -> {
           files.add(Converter.FileDescriptionToFile(fileDescription));
        });
        return files;
    }

    @GetMapping("/download/getallTest")
    public Set<FileDescription> getAllFilesTest() {
        Set<FileDescription> files = new HashSet<>(fileManagementService.getAllFiles());
        return files;
    }

    @GetMapping("/download/getAllByService/{services}")
    public Set<File> getAllFilesByService(@RequestParam String services) {
        Set<FileDescription> files = new HashSet<>(fileManagementService.getAllFilesByServices(services));
        Set<File> result = new HashSet<>(Converter.FileDescriptionsToFiles(files));
        return result;
    }

}
