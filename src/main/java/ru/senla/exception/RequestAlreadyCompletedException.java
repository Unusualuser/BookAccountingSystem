package ru.senla.exception;

public class RequestAlreadyCompletedException extends RuntimeException {
    public RequestAlreadyCompletedException(String message) {
        super(message);
    }
}
