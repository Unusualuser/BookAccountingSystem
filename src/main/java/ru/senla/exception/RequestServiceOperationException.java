package ru.senla.exception;

public class RequestServiceOperationException extends RuntimeException {
    public RequestServiceOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
