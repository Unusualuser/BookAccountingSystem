package ru.senla.exception;

public class BookHistoryServiceOperationException extends RuntimeException {
    public BookHistoryServiceOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
