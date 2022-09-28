package ru.example.exception;

public class BookAvailableException extends RuntimeException {
    public BookAvailableException(String message) {
        super(message);
    }
}
