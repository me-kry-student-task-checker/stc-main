package hu.me.iit.malus.thesis.filemanagement.service.converters;

import hu.me.iit.malus.thesis.filemanagement.controller.dto.FileDescriptorDto;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescriptor;

import java.util.HashSet;
import java.util.Set;

public class Converter {

    public static FileDescriptorDto createFileDescriptorDtoFromFileDescriptor(FileDescriptor fileDescriptor) {
        FileDescriptorDto fileDescriptorDto = new FileDescriptorDto();
        fileDescriptorDto.setId(fileDescriptor.getId());
        fileDescriptorDto.setName(fileDescriptor.getName());
        fileDescriptorDto.setDownloadLink(fileDescriptor.getDownloadLink());
        fileDescriptorDto.setUploadDate(fileDescriptor.getUploadDate());
        fileDescriptorDto.setUploadedBy(fileDescriptor.getUploadedBy());
        fileDescriptorDto.setContentType(fileDescriptor.getContentType());
        fileDescriptorDto.setTagId(fileDescriptor.getTagId());
        return fileDescriptorDto;
    }

    public static Set<FileDescriptorDto> createFileDescriptorDtosFromFileDescriptors(Set<FileDescriptor> fileDescriptors) {
        Set<FileDescriptorDto> result = new HashSet<>();
        for (FileDescriptor fd : fileDescriptors) {
            result.add(createFileDescriptorDtoFromFileDescriptor(fd));
        }
        return result;
    }


}
