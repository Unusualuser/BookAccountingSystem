package ru.senla.dto;

import java.io.Serializable;

public class AddQuantityByBookIdRequestDTO implements Serializable {
    private Long bookId;
    private Long quantityToAdd;

    public AddQuantityByBookIdRequestDTO() {
    }

    public AddQuantityByBookIdRequestDTO(Long bookId, Long quantityToAdd) {
        this.bookId = bookId;
        this.quantityToAdd = quantityToAdd;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getQuantityToAdd() {
        return quantityToAdd;
    }

    public void setQuantityToAdd(Long quantityToAdd) {
        this.quantityToAdd = quantityToAdd;
    }
}
