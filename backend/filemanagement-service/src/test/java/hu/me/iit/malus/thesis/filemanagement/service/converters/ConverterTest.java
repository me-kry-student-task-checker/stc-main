package hu.me.iit.malus.thesis.filemanagement.service.converters;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;

import hu.me.iit.malus.thesis.filemanagement.controller.dto.FileDescriptorDto;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescriptor;
import hu.me.iit.malus.thesis.filemanagement.model.ServiceType;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.junit.Test;

public class ConverterTest {

  @Test
  public void testCreateFileDescriptorDtoFromFileDescriptor_ok() throws ParseException {

    // GIVEN
    Long testId = 1L;
    String testName = "testName";
    String testLink = "testLink";
    long testSize = 1024L;
    Date testUploadDate = new DateTime(2021, 10, 11, 0, 0).toDate();
    String testUploader = "testUploader";
    String testContentType = "testContentType";
    ServiceType testServiceType = ServiceType.COURSE;
    Long testTagId = 12L;
    boolean testRemoved = false;

    FileDescriptor fileDescriptor = new FileDescriptor(testId, testName, testLink, testSize,
        testUploadDate, testUploader,
        testContentType, testServiceType, testTagId,testRemoved);

    // WHEN
    FileDescriptorDto result = Converter.createFileDescriptorDtoFromFileDescriptor(fileDescriptor);

    // THEN
    assertNotEquals(null, result);
    assertEquals(testId, result.getId());
    assertEquals(testName, result.getName());
    assertEquals(testLink, result.getDownloadLink());
    assertEquals(testUploadDate, result.getUploadDate());
    assertEquals(testUploader, result.getUploadedBy());
    assertEquals(testContentType, result.getContentType());
    assertEquals(testTagId, result.getTagId());
  }

  @Test
  public void testCreateFileDescriptorDtoFromFileDescriptor_null() throws ParseException {

    // GIVEN

    FileDescriptor fileDescriptor = null;

    // WHEN
    try {
      FileDescriptorDto result = Converter
          .createFileDescriptorDtoFromFileDescriptor(fileDescriptor);
      fail("Exception not thrown");
    } catch (NullPointerException e) {
      e.printStackTrace();
    }

    // THEN

  }

  @Test
  public void testCreateFileDescriptorDtoListFromFileDescriptorList_ok() {
    // GIVEN
    Long testId = 1L;
    String testName = "testName";
    String testLink = "testLink";
    Long testSize = 1024L;
    Date testUploadDate = new DateTime(2021, 10, 11, 0, 0).toDate();
    String testUploader = "testUploader";
    String testContentType = "testContentType";
    ServiceType testServiceType = ServiceType.COURSE;
    Long testTagId = 12L;
    boolean testRemoved = false;

    FileDescriptor fileDescriptor = new FileDescriptor(testId, testName, testLink, testSize,
        testUploadDate, testUploader,
        testContentType, testServiceType, testTagId, testRemoved);

    List<FileDescriptor> fileDescriptorList = new ArrayList<>();
    fileDescriptorList.add(fileDescriptor);

    // WHEN
    List<FileDescriptorDto> result = Converter
        .createFileDescriptorDtoListFromFileDescriptorList(fileDescriptorList);

    // THEN

    assertThat(result, hasSize(1));

    assertNotEquals(null, result);
    assertEquals(testId, result.get(0).getId());
    assertEquals(testName, result.get(0).getName());
    assertEquals(testLink, result.get(0).getDownloadLink());
    assertEquals(testUploadDate, result.get(0).getUploadDate());
    assertEquals(testUploader, result.get(0).getUploadedBy());
    assertEquals(testContentType, result.get(0).getContentType());
    assertEquals(testTagId, result.get(0).getTagId());
  }

  @Test
  public void testCreateFileDescriptorDtoListFromFileDescriptorList_empty() {
    // GIVEN

    List<FileDescriptor> fileDescriptorList = new ArrayList<>();

    // WHEN
      List<FileDescriptorDto> result = Converter
          .createFileDescriptorDtoListFromFileDescriptorList(fileDescriptorList);

    // THEN

    assertThat(result, hasSize(0));
  }

  @Test
  public void testCreateFileDescriptorDtoListFromFileDescriptorList_null() {
    // GIVEN

    List<FileDescriptor> fileDescriptorList = null;

    // WHEN
    try {
      List<FileDescriptorDto> result = Converter
          .createFileDescriptorDtoListFromFileDescriptorList(fileDescriptorList);
      fail("Exception not thrown");
    } catch (NullPointerException e) {
      e.printStackTrace();
    }

    // THEN
  }
}