package ru.senla.dto;

import java.io.Serializable;

public class AddQuantityByBookIdResponseDto implements Serializable {
    private Long bookId;
    private Long addedQuantity;

    public AddQuantityByBookIdResponseDto() {
    }

    public AddQuantityByBookIdResponseDto(Long bookId, Long addedQuantity) {
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
