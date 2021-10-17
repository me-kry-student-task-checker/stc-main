package hu.me.iit.malus.thesis.filemanagement.service.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import hu.me.iit.malus.thesis.filemanagement.controller.dto.FileDescriptorDto;
import hu.me.iit.malus.thesis.filemanagement.model.FileDescriptor;
import hu.me.iit.malus.thesis.filemanagement.model.ServiceType;
import hu.me.iit.malus.thesis.filemanagement.repository.FileDescriptorRepository;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.FileNotFoundException;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.ForbiddenFileDeleteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.junit.Test;

public class FileManagementServiceImplBaseTest {

  @Test
  public void testGetAllFilesByUser() {
    // GIVEN
    FileDescriptorRepository mockFileDescriptorRepository = mock(FileDescriptorRepository.class);

    FileManagementServiceImplBase mockClass = mock(FileManagementServiceImplBase.class,
        withSettings().useConstructor(mockFileDescriptorRepository)
            .defaultAnswer(CALLS_REAL_METHODS));
    String testUserEmail = "testMail";

    List<FileDescriptor> testFileDescriptorList = new ArrayList<>();
    Long testId = 1L;
    String testName = "testName";
    String testLink = "testLink";
    long testSize = 1024L;
    Date testUploadDate = new DateTime(2021, 10, 11, 0, 0).toDate();
    String testUploader = "testUploader";
    String testContentType = "testContentType";
    ServiceType testServiceType = ServiceType.COURSE;
    Long testTagId = 12L;

    FileDescriptor fileDescriptor = new FileDescriptor(testId, testName, testLink, testSize,
        testUploadDate, testUploader,
        testContentType, testServiceType, testTagId);
    testFileDescriptorList.add(fileDescriptor);

    when(mockFileDescriptorRepository.findAllByUploadedBy(testUserEmail))
        .thenReturn(testFileDescriptorList);

    doCallRealMethod().when(mockClass).getAllFilesByUser(testUserEmail);

    // WHEN

    List<FileDescriptorDto> resultList = mockClass.getAllFilesByUser(testUserEmail);

    // THEN
    verify(mockFileDescriptorRepository)
        .findAllByUploadedBy(testUserEmail);

    assertThat(resultList, hasSize(1));
    assertNotEquals(null, resultList);
    assertEquals(testId, resultList.get(0).getId());
    assertEquals(testName, resultList.get(0).getName());
    assertEquals(testLink, resultList.get(0).getDownloadLink());
    assertEquals(testUploadDate, resultList.get(0).getUploadDate());
    assertEquals(testUploader, resultList.get(0).getUploadedBy());
    assertEquals(testContentType, resultList.get(0).getContentType());
    assertEquals(testTagId, resultList.get(0).getTagId());
  }

  @Test
  public void testGetAllFilesByServiceTypeAndTagId() {
    FileDescriptorRepository mockFileDescriptorRepository = mock(FileDescriptorRepository.class);

    FileManagementServiceImplBase mockClass = mock(FileManagementServiceImplBase.class,
        withSettings().useConstructor(mockFileDescriptorRepository)
            .defaultAnswer(CALLS_REAL_METHODS));

    List<FileDescriptor> testFileDescriptorList = new ArrayList<>();
    Long testId = 1L;
    String testName = "testName";
    String testLink = "testLink";
    long testSize = 1024L;
    Date testUploadDate = new DateTime(2021, 10, 11, 0, 0).toDate();
    String testUploader = "testUploader";
    String testContentType = "testContentType";
    ServiceType testServiceType = ServiceType.COURSE;
    Long testTagId = 12L;

    FileDescriptor fileDescriptor = new FileDescriptor(testId, testName, testLink, testSize,
        testUploadDate, testUploader,
        testContentType, testServiceType, testTagId);
    testFileDescriptorList.add(fileDescriptor);

    when(mockFileDescriptorRepository.findAllByServiceTypeAndTagId(testServiceType, testTagId))
        .thenReturn(testFileDescriptorList);

    doCallRealMethod().when(mockClass).getAllFilesByServiceTypeAndTagId(testTagId, testServiceType);

    // WHEN

    List<FileDescriptorDto> resultList = mockClass
        .getAllFilesByServiceTypeAndTagId(testTagId, testServiceType);

    // THEN
    verify(mockFileDescriptorRepository)
        .findAllByServiceTypeAndTagId(testServiceType, testTagId);

    assertThat(resultList, hasSize(1));
    assertNotEquals(null, resultList);
    assertEquals(testId, resultList.get(0).getId());
    assertEquals(testName, resultList.get(0).getName());
    assertEquals(testLink, resultList.get(0).getDownloadLink());
    assertEquals(testUploadDate, resultList.get(0).getUploadDate());
    assertEquals(testUploader, resultList.get(0).getUploadedBy());
    assertEquals(testContentType, resultList.get(0).getContentType());
    assertEquals(testTagId, resultList.get(0).getTagId());
  }

  @Test
  public void testDeleteFilesByServiceAndTagId()
      throws FileNotFoundException, ForbiddenFileDeleteException {
    FileDescriptorRepository mockFileDescriptorRepository = mock(FileDescriptorRepository.class);

    FileManagementServiceImplBase mockClass = mock(FileManagementServiceImplBase.class,
        withSettings().useConstructor(mockFileDescriptorRepository)
            .defaultAnswer(CALLS_REAL_METHODS));

    String testUserEmail = "testMail";
    String testRole = "testRole";

    List<FileDescriptor> testFileDescriptorList = new ArrayList<>();
    Long testId = 1L;
    String testName = "testName";
    String testLink = "testLink";
    long testSize = 1024L;
    Date testUploadDate = new DateTime(2021, 10, 11, 0, 0).toDate();
    String testUploader = "testUploader";
    String testContentType = "testContentType";
    ServiceType testServiceType = ServiceType.COURSE;
    Long testTagId = 12L;

    FileDescriptor fileDescriptor = new FileDescriptor(testId, testName, testLink, testSize,
        testUploadDate, testUploader,
        testContentType, testServiceType, testTagId);
    testFileDescriptorList.add(fileDescriptor);

    when(mockFileDescriptorRepository.findAllByServiceTypeAndTagId(testServiceType, testTagId))
        .thenReturn(testFileDescriptorList);

    FileDescriptorDto expectedResultElement = new FileDescriptorDto();
    expectedResultElement.setId(testId);
    expectedResultElement.setName(testName);
    expectedResultElement.setDownloadLink(testLink);
    expectedResultElement.setUploadDate(testUploadDate);
    expectedResultElement.setUploadedBy(testUploader);
    expectedResultElement.setContentType(testContentType);
    expectedResultElement.setTagId(testTagId);

    doCallRealMethod().when(mockClass)
        .deleteFilesByServiceAndTagId(testServiceType, testTagId, testUserEmail, testRole);

    // WHEN

    mockClass.deleteFilesByServiceAndTagId(testServiceType, testTagId, testUserEmail, testRole);

    // THEN
    verify(mockFileDescriptorRepository)
        .findAllByServiceTypeAndTagId(testServiceType, testTagId);

    verify(mockClass, times(1)).deleteFile(anyLong(), any(), anyString(), anyString());
  }
}