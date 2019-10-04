package hu.me.iit.malus.thesis.task.controller.dto;

public class TaskDto {

    private Long id;
    private String name;
    private String description;
    private Long courseId;

    public TaskDto() {
    }

    public TaskDto(String name, String description, Long courseId) {
        this.name = name;
        this.description = description;
        this.courseId = courseId;
    }

    public TaskDto(Long id, String name, String description, Long courseId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.courseId = courseId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }


}
