package ru.example.exception;

public class BookHistoryNotFoundException extends RuntimeException {
    public BookHistoryNotFoundException(String message) {
        super(message);
    }
}
