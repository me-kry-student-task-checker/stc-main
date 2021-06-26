package hu.me.iit.malus.thesis.feedback.service.converters;

import hu.me.iit.malus.thesis.feedback.controller.dto.CourseCommentCreateDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.CourseCommentDetailsDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.TaskCommentCreateDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.TaskCommentDetailsDto;
import hu.me.iit.malus.thesis.feedback.model.CourseComment;
import hu.me.iit.malus.thesis.feedback.model.TaskComment;

import java.util.HashSet;

public class DtoConverter {

    private DtoConverter() {
    }

    public static CourseComment courseCommentCreateDtoToCourseComment(CourseCommentCreateDto dto) {
        CourseComment courseComment = new CourseComment();
        courseComment.setFiles(new HashSet<>());
        courseComment.setCourseId(dto.getCourseId());
        courseComment.setText(dto.getText());
        return courseComment;
    }

    public static TaskComment taskCommentCreateDtoToTaskComment(TaskCommentCreateDto dto) {
        TaskComment taskComment = new TaskComment();
        taskComment.setFiles(new HashSet<>());
        taskComment.setTaskId(dto.getTaskId());
        taskComment.setText(dto.getText());
        return taskComment;
    }

    public static CourseCommentDetailsDto courseCommentToCourseCommentDetailsDto(CourseComment comment) {
        CourseCommentDetailsDto dto = new CourseCommentDetailsDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthorId(comment.getAuthorId());
        dto.setCreateDate(comment.getCreateDate());
        dto.setFiles(comment.getFiles());
        dto.setCourseId(comment.getCourseId());
        return dto;
    }

    public static TaskCommentDetailsDto taskCommentToTaskCommentDetailsDto(TaskComment comment) {
        TaskCommentDetailsDto dto = new TaskCommentDetailsDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthorId(comment.getAuthorId());
        dto.setCreateDate(comment.getCreateDate());
        dto.setFiles(comment.getFiles());
        dto.setTaskId(comment.getTaskId());
        return dto;
    }
}



