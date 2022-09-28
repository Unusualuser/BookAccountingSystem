package ru.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BookStorageIllegalReduceQuantityException extends RuntimeException {
    public BookStorageIllegalReduceQuantityException(Long bookId) {
        super(String.format("Невозможно уменьшить количество книги с id %d, так как её количества недостаточно.", bookId));
    }
}
