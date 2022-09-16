package ru.senla.exception;

public class BookAvailableException extends RuntimeException {
    public BookAvailableException(String message) {
        super(message);
    }
}
