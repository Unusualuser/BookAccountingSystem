package ru.example.dto;

import java.io.Serializable;

public class GetQuantityByBookIdResponseDto implements Serializable {
    private Long bookId;
    private Long quantity;

    public GetQuantityByBookIdResponseDto() {
    }

    public GetQuantityByBookIdResponseDto(Long bookId, Long quantity) {
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
