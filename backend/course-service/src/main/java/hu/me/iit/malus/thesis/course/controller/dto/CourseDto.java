package hu.me.iit.malus.thesis.course.controller.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CourseDto {

    private Long id;
    private String name;
    private String description;
    private String creator;


    public CourseDto(String name, String description) {
        id = null;
        this.name = name;
        this.description = description;
    }

    public CourseDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCreator() {
        return creator;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Course: " +
                "name:'" + name + '\'' +
                ", description'" + description + '\'' +
                ", creator" + creator +
                '}';
    }
}
