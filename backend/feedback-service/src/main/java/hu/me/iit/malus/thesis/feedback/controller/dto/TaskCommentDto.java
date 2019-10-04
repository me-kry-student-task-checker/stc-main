package hu.me.iit.malus.thesis.feedback.controller.dto;


public class TaskCommentDto {

    private String authorId;
    private String text;
    private Long taskId;

    public TaskCommentDto(String authorId, String text, Long taskId) {
        this.authorId = authorId;
        this.text = text;
        this.taskId = taskId;
    }

    public TaskCommentDto() {
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

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
