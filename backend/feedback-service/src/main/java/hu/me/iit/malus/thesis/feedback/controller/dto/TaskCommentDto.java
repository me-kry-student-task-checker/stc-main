package hu.me.iit.malus.thesis.feedback.controller.dto;


public class TaskCommentDto {

    private String text;
    private Long taskId;

    public TaskCommentDto(String text, Long taskId) {
        this.text = text;
        this.taskId = taskId;
    }

    public TaskCommentDto() {
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
