package hu.me.iit.malus.thesis.feedback.controller.converters;

import hu.me.iit.malus.thesis.feedback.controller.dto.CourseCommentDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.TaskCommentDto;
import hu.me.iit.malus.thesis.feedback.model.CourseComment;
import hu.me.iit.malus.thesis.feedback.model.TaskComment;

import java.util.HashSet;

public class Converter {

    public static CourseComment CourseCommentDtoToCourseComment(CourseCommentDto courseCommentDto) {
        CourseComment courseComment = new CourseComment();
        courseComment.setFiles(new HashSet<>());
        courseComment.setCourseId(courseCommentDto.getCourseId());
        courseComment.setAuthorId(courseCommentDto.getAuthorId());
        courseComment.setText(courseCommentDto.getText());
        return courseComment;
    }

    public static TaskComment TaskCommentDtoToTaskComment(TaskCommentDto taskCommentDto) {
        TaskComment taskComment = new TaskComment();
        taskComment.setFiles(new HashSet<>());
        taskComment.setTaskId(taskCommentDto.getTaskId());
        taskComment.setAuthorId(taskCommentDto.getAuthorId());
        taskComment.setText(taskCommentDto.getText());
        return taskComment;
    }
}

