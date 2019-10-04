package hu.me.iit.malus.thesis.feedback.controller.dto;

import java.util.Date;

public class CourseCommentDto {

    private String authorId;
    private String text;
    private Long courseId;

    public CourseCommentDto(String authorId, String text, Long courseId) {
        this.authorId = authorId;
        this.text = text;
        this.courseId = courseId;
    }

    public CourseCommentDto() {
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
