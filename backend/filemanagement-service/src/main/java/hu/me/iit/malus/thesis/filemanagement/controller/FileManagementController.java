package hu.me.iit.malus.thesis.filemanagement.controller;


import hu.me.iit.malus.thesis.filemanagement.controller.converters.Converter;
import hu.me.iit.malus.thesis.filemanagement.controller.dto.File;
import hu.me.iit.malus.thesis.filemanagement.controller.dto.Service;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescription;
import hu.me.iit.malus.thesis.filemanagement.service.FileManagementService;
import org.apache.http.HttpStatus;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.FormParam;
import java.io.IOException;
import java.util.*;

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
    public File uploadFile(@FormDataParam("file") Part file, @FormParam("service") Service service) throws IOException {
        //TODO: Replace with users got from header
        FileDescription fd = fileManagementService.uploadFile(file, service, "krsztn@alma.hu");
        if (fd == null) return null;
        return Converter.FileDescriptionToFile(fd);
    }

    @DeleteMapping("/delete/{id}/{service}")
    public void deleteFile(@PathVariable Long id, @PathVariable Service service) {
        fileManagementService.deleteFile(id, service);
    }

    @GetMapping("/download/getById/{id}")
    public File getFileById(@PathVariable Long id, HttpServletResponse response){
        FileDescription fd = fileManagementService.getById(id);
        if(fd == null) {
            response.setStatus( HttpStatus.SC_NO_CONTENT);
            return null;
        }
        return Converter.FileDescriptionToFile(fd);
    }

    @GetMapping("/download/getByFilename/{filename}")
    public Set<File> getFileByFileName(@PathVariable String filename){
        Set<File> files = new HashSet<>();
        fileManagementService.getAllByFileName(filename).forEach(fd -> {
            files.add(Converter.FileDescriptionToFile(fd));
        });
        return files;
    }

    @GetMapping("/download/getByUser/{user}")
    public Set<File> getFileByFileUser(@PathVariable String user){
        Set<File> files = new HashSet<>();
        fileManagementService.getAllFilesByUsers(user).forEach(fd -> {
            files.add(Converter.FileDescriptionToFile(fd));
        });
        return files;
    }

    @GetMapping("/download/getByService/{service}")
    public Set<File> getAllFilesByService(@PathVariable Service service) {
        Set<FileDescription> files = new HashSet<>(fileManagementService.getAllFilesByServices(service));
        return new HashSet<>(Converter.FileDescriptionsToFiles(files));
    }

    @GetMapping("/download/getAll")
    public Set<File> getAllFiles() {
        Set<File> files = new HashSet<>();
        fileManagementService.getAllFiles().forEach(fileDescription -> {
           files.add(Converter.FileDescriptionToFile(fileDescription));
        });
        return files;
    }
}
