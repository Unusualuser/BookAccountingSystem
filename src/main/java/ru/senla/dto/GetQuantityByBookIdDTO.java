package ru.senla.dto;

import java.io.Serializable;

public class GetQuantityByBookIdDTO implements Serializable {
    private Long bookId;
    private Long quantity;

    public GetQuantityByBookIdDTO() {
    }

    public GetQuantityByBookIdDTO(Long bookId, Long quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
