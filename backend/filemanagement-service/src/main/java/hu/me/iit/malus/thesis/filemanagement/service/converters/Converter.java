package hu.me.iit.malus.thesis.filemanagement.service.converters;

import hu.me.iit.malus.thesis.filemanagement.controller.dto.FileDescriptorDto;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescriptor;

import java.util.List;
import java.util.stream.Collectors;

public class Converter {

    private Converter() {
    }

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

    public static List<FileDescriptorDto> createFileDescriptorDtoListFromFileDescriptorList(List<FileDescriptor> fileDescriptors) {
        return fileDescriptors.stream().map(Converter::createFileDescriptorDtoFromFileDescriptor).collect(Collectors.toList());
    }
}
