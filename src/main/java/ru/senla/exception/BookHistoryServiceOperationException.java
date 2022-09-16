package ru.senla.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BookHistoryServiceOperationException extends RuntimeException {
    public BookHistoryServiceOperationException(String message) {
        super(message);
    }
}
