package hu.me.iit.malus.thesis.feedback.controller.dto;


public class CourseCommentDto {

    private String text;
    private Long courseId;

    public CourseCommentDto(String text, Long courseId) {
        this.text = text;
        this.courseId = courseId;
    }

    public CourseCommentDto() {
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
