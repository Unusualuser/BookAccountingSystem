package ru.senla.exception;

public class BookStorageServiceOperationException extends RuntimeException {
    public BookStorageServiceOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
