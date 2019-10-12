package hu.me.iit.malus.thesis.filemanagement.controller.converters;

import hu.me.iit.malus.thesis.filemanagement.controller.dto.File;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescription;

import java.util.HashSet;
import java.util.Set;

public class Converter {

    public static File FileDescriptionToFile(FileDescription fileDescription) {
        File file = new File();
        file.setId(fileDescription.getId());
        file.setName(fileDescription.getName());
        file.setDownloadLink(fileDescription.getDownloadLink());
        file.setUploadDate(fileDescription.getUploadDate());
        file.setUploadedBy(fileDescription.getUploadedBy());
        file.setContentType(fileDescription.getContentType());
        file.setTagId(fileDescription.getTagId());
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
