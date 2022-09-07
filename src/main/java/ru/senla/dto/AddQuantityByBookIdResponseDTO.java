package ru.senla.dto;

import java.io.Serializable;

public class AddQuantityByBookIdResponseDTO implements Serializable {
    private Long bookId;
    private Long addedQuantity;

    public AddQuantityByBookIdResponseDTO() {
    }

    public AddQuantityByBookIdResponseDTO(Long bookId, Long addedQuantity) {
        this.bookId = bookId;
        this.addedQuantity = addedQuantity;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getAddedQuantity() {
        return addedQuantity;
    }

    public void setAddedQuantity(Long addedQuantity) {
        this.addedQuantity = addedQuantity;
    }
}
