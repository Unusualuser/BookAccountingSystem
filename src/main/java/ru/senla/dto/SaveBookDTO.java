package ru.senla.dto;

import java.io.Serializable;

public class SaveBookDTO implements Serializable {
    private String name;
    private Integer publicationYear;
    private String author;
    private String description;

    public SaveBookDTO() {
    }

    public SaveBookDTO(String name, Integer publicationYear, String author, String description) {
        this.name = name;
        this.publicationYear = publicationYear;
        this.author = author;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
