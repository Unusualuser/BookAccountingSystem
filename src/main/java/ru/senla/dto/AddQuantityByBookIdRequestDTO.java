package ru.senla.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class AddQuantityByBookIdRequestDTO implements Serializable {
    @NotNull
    @Min(value = 0L, message = "Значение bookId должно быть положительным")
    private Long bookId;
    @NotNull
    @Min(value = 0L, message = "Значение quantityToAdd должно быть положительным")
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
