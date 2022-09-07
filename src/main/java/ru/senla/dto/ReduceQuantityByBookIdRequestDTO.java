package ru.senla.dto;

import java.io.Serializable;

public class ReduceQuantityByBookIdRequestDTO implements Serializable {
    private Long bookId;
    private Long quantityToReduce;

    public ReduceQuantityByBookIdRequestDTO() {
    }

    public ReduceQuantityByBookIdRequestDTO(Long bookId, Long quantityToReduce) {
        this.bookId = bookId;
        this.quantityToReduce = quantityToReduce;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getQuantityToReduce() {
        return quantityToReduce;
    }

    public void setQuantityToReduce(Long quantityToReduce) {
        this.quantityToReduce = quantityToReduce;
    }
}
