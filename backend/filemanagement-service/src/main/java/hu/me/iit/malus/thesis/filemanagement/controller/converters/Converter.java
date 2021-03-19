package hu.me.iit.malus.thesis.filemanagement.controller.converters;

import hu.me.iit.malus.thesis.filemanagement.controller.dto.FileDescriptorDto;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescription;

import java.util.HashSet;
import java.util.Set;

public class Converter {

    public static FileDescriptorDto FileDescriptionToFile(FileDescription fileDescription) {
        FileDescriptorDto fileDescriptorDto = new FileDescriptorDto();
        fileDescriptorDto.setId(fileDescription.getId());
        fileDescriptorDto.setName(fileDescription.getName());
        fileDescriptorDto.setDownloadLink(fileDescription.getDownloadLink());
        fileDescriptorDto.setUploadDate(fileDescription.getUploadDate());
        fileDescriptorDto.setUploadedBy(fileDescription.getUploadedBy());
        fileDescriptorDto.setContentType(fileDescription.getContentType());
        fileDescriptorDto.setTagId(fileDescription.getTagId());
        return fileDescriptorDto;
    }

    public static Set<FileDescriptorDto> FileDescriptionsToFiles(Set<FileDescription> fileDescriptions) {
        Set<FileDescriptorDto> result = new HashSet<>();
        for (FileDescription fd : fileDescriptions) {
            result.add(FileDescriptionToFile(fd));
        }
        return result;
    }


}
