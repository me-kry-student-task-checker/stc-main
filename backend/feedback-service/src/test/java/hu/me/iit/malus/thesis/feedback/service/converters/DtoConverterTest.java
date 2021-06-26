package hu.me.iit.malus.thesis.feedback.service.converters;

import hu.me.iit.malus.thesis.dto.File;
import hu.me.iit.malus.thesis.feedback.controller.dto.CourseCommentCreateDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.CourseCommentDetailsDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.TaskCommentCreateDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.TaskCommentDetailsDto;
import hu.me.iit.malus.thesis.feedback.model.CourseComment;
import hu.me.iit.malus.thesis.feedback.model.TaskComment;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DtoConverterTest {

    @Test
    public void courseCommentCreateDtoToCourseComment() {
        String testText = "3UERp";
        long testCourseId = 3L;
        CourseComment comment = DtoConverter.courseCommentCreateDtoToCourseComment(new CourseCommentCreateDto(testText, testCourseId));
        assertThat(comment.getCourseId(), is(testCourseId));
        assertThat(comment.getText(), is(testText));
        assertThat(comment.getFiles(), is(new HashSet<>()));
    }

    @Test
    public void taskCommentCreateDtoToTaskComment() {
        String testText = "RQ2RMRG";
        long testTaskId = 37L;
        TaskComment comment = DtoConverter.taskCommentCreateDtoToTaskComment(new TaskCommentCreateDto(testText, testTaskId));
        assertThat(comment.getTaskId(), is(testTaskId));
        assertThat(comment.getText(), is(testText));
        assertThat(comment.getFiles(), is(new HashSet<>()));
    }

    @Test
    public void courseCommentToCourseCommentDetailsDto() {
        long testId = 130L;
        Date testDate = new Date();
        String testAuthorId = "2vM6kN9";
        String testText = "NBM";
        long testCourseId = 979L;
        HashSet<File> testFiles = new HashSet<>();
        CourseComment comment = new CourseComment();
        comment.setId(testId);
        comment.setCreateDate(testDate);
        comment.setAuthorId(testAuthorId);
        comment.setText(testText);
        comment.setCourseId(testCourseId);
        comment.setFiles(testFiles);

        CourseCommentDetailsDto dto = DtoConverter.courseCommentToCourseCommentDetailsDto(comment);

        assertThat(dto.getId(), is(testId));
        assertThat(dto.getText(), is(testText));
        assertThat(dto.getCreateDate(), is(testDate));
        assertThat(dto.getCourseId(), is(testCourseId));
        assertThat(dto.getAuthorId(), is(testAuthorId));
        assertThat(dto.getFiles(), is(testFiles));
    }

    @Test
    public void taskCommentToTaskCommentDetailsDto() {
        long testId = 130L;
        Date testDate = new Date();
        String testAuthorId = "2vM6kN9";
        String testText = "NBM";
        long testTaskId = 979L;
        HashSet<File> testFiles = new HashSet<>();
        TaskComment comment = new TaskComment();
        comment.setId(testId);
        comment.setCreateDate(testDate);
        comment.setAuthorId(testAuthorId);
        comment.setText(testText);
        comment.setTaskId(testTaskId);
        comment.setFiles(testFiles);

        TaskCommentDetailsDto dto = DtoConverter.taskCommentToTaskCommentDetailsDto(comment);

        assertThat(dto.getId(), is(testId));
        assertThat(dto.getText(), is(testText));
        assertThat(dto.getCreateDate(), is(testDate));
        assertThat(dto.getTaskId(), is(testTaskId));
        assertThat(dto.getAuthorId(), is(testAuthorId));
        assertThat(dto.getFiles(), is(testFiles));
    }
}