package ru.example.dto;

import java.io.Serializable;

public class ReduceQuantityByBookIdResponseDto implements Serializable {
    private Long bookId;
    private Long reducedQuantity;

    public ReduceQuantityByBookIdResponseDto() {
    }

    public ReduceQuantityByBookIdResponseDto(Long bookId, Long reducedQuantity) {
        this.bookId = bookId;
        this.reducedQuantity = reducedQuantity;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getReducedQuantity() {
        return reducedQuantity;
    }

    public void setReducedQuantity(Long reducedQuantity) {
        this.reducedQuantity = reducedQuantity;
    }
}