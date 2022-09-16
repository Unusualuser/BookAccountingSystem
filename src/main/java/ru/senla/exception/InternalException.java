package ru.senla.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalException extends RuntimeException {
    public InternalException() {
        super("Внутренняя ошибка.");
    }
}
