package ru.senla.dto;

import java.io.Serializable;

public class ReduceQuantityByBookIdResponseDTO implements Serializable {
    private Long bookId;
    private Long reducedQuantity;

    public ReduceQuantityByBookIdResponseDTO() {
    }

    public ReduceQuantityByBookIdResponseDTO(Long bookId, Long reducedQuantity) {
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