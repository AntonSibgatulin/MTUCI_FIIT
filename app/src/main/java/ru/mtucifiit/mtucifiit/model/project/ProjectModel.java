package ru.mtucifiit.mtucifiit.model.project;

public class ProjectModel {

    public Long id;
    public String name;
    public String type;
    public String description;
    public String author;


    public ProjectModel(Long id, String name, String type, String description, String author) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.author = author;
    }

    public ProjectModel() {
    }
}
