package ru.example.dto;

public class IdDto {
    private Long id;

    public IdDto() {
    }

    public IdDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
