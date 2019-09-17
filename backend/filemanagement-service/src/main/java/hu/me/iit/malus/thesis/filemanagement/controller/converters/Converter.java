package hu.me.iit.malus.thesis.filemanagement.controller.converters;

import hu.me.iit.malus.thesis.filemanagement.controller.dto.File;
import hu.me.iit.malus.thesis.filemanagement.controller.dto.Service;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescription;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Converter {

    public static File FileDescriptionToFile(FileDescription fileDescription) {
        File file = new File();
        file.setName(fileDescription.getName());
        file.setDownloadLink(fileDescription.getDownloadLink());
        file.setSize(fileDescription.getSize());
        //TODO: As user authentication up it should be evaluated
        file.setCanBeDeleted(true);
        file.setUploadDate(fileDescription.getUploadDate());
        file.setContentType(fileDescription.getContentType());
        file.setServices(fileDescription.getServices());
        return file;
    }

    public static Set<File> FileDescriptionsToFiles(Set<FileDescription> fileDescriptions) {
        Set<File> result = new HashSet<>();
        for (FileDescription fd : fileDescriptions) {
            result.add(FileDescriptionToFile(fd));
        }
        return result;
    }



}
