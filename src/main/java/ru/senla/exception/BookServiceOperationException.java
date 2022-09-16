package ru.senla.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BookServiceOperationException extends RuntimeException {
    public BookServiceOperationException(String message) {
        super(message);
    }
}
