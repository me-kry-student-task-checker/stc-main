package hu.me.iit.malus.thesis.filemanagement.controller;


import hu.me.iit.malus.thesis.filemanagement.controller.dto.File;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescription;
import hu.me.iit.malus.thesis.filemanagement.model.exceptions.FileCouldNotBeUploaded;
import hu.me.iit.malus.thesis.filemanagement.service.FileManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Controller endpoint of this service, which handles u
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
    public File send(@RequestBody Part file) throws FileCouldNotBeUploaded, IOException {
        FileDescription fd = fileManagementService.uploadFile(file.getName(), file.getSubmittedFileName(), file.getSize(), file.getInputStream());
        File myFile = new File();
        myFile.setDownloadLink(fd.getDownloadLink());
        myFile.setName(fd.getName());
        myFile.setSize(fd.getSize());
        myFile.setSubmittedName(fd.getSubmittedName());
        return myFile;
    }

    @DeleteMapping("/delete/{id}")
    public void send(@RequestParam Long id) {
        fileManagementService.deleteFile(id);
    }

    @GetMapping("/download/{id}")
    public File getFileById(@PathVariable Long id){
        FileDescription fd = fileManagementService.getFileById(id);
        File file = new File();
        file.setSubmittedName(fd.getSubmittedName());
        file.setSize(fd.getSize());
        file.setName(fd.getName());
        file.setDownloadLink(fd.getDownloadLink());
        return file;
    }

    @GetMapping("/download/{filename}")
    public Set<File> getFileByFileName(@PathVariable String filename){
        Set<File> files = new HashSet<>();

        fileManagementService.getFileByFileName(filename).stream().forEach(fileDescription -> {
            File file = new File();
            file.setDownloadLink(fileDescription.getDownloadLink());
            file.setName(fileDescription.getName());
            file.setSize(fileDescription.getSize());
            file.setSubmittedName(fileDescription.getSubmittedName());
            files.add(file);
        });

        return files;
    }

    @GetMapping("/download/getall")
    public Set<File> getAllFiles() {
        Set<File> files = new HashSet<>();

        fileManagementService.getAllFiles().stream().forEach(fileDescription -> {
            File file = new File();
            file.setDownloadLink(fileDescription.getDownloadLink());
            file.setName(fileDescription.getName());
            file.setSize(fileDescription.getSize());
            file.setSubmittedName(fileDescription.getSubmittedName());
            files.add(file);
        });
        return files;
    }
}
