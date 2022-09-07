package ru.senla.exception;

public class BookServiceOperationException extends RuntimeException {
    public BookServiceOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
