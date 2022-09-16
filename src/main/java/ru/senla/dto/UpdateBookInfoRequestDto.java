package ru.senla.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class UpdateBookInfoRequestDto implements Serializable {
    @NotNull
    @Min(value = 0L, message = "Значение id должно быть положительным")
    private Long id;
    @Size(min = 4, max = 30, message = "Имя должно быть не меньше 4 и не больше 30 символов")
    private String name;
    @Min(value = 0, message = "Год публикации должен быть положительным")
    private Integer publicationYear;
    @Size(min = 4, max = 100, message = "Имя автора должно быть не меньше 4 и не больше 100 символов")
    private String author;
    @Size(max = 70, message = "Описание должно быть не больше 70 символов")
    private String description;

    public UpdateBookInfoRequestDto() {
    }

    public UpdateBookInfoRequestDto(Long id, String name, Integer publicationYear, String author, String description) {
        this.id = id;
        this.name = name;
        this.publicationYear = publicationYear;
        this.author = author;
        this.description = description;
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
