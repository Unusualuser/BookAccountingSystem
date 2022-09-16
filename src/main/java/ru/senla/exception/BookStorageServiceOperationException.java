package ru.senla.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BookStorageServiceOperationException extends RuntimeException {
    public BookStorageServiceOperationException(String message) {
        super(message);
    }
}
