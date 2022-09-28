package ru.example.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ReduceQuantityByBookIdRequestDto implements Serializable {
    @NotNull
    @Min(value = 0L, message = "Значение bookId должно быть положительным")
    private Long bookId;
    @NotNull
    @Min(value = 0L, message = "Значение quantityToReduce должно быть положительным")
    private Long quantityToReduce;

    public ReduceQuantityByBookIdRequestDto() {
    }

    public ReduceQuantityByBookIdRequestDto(Long bookId, Long quantityToReduce) {
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
