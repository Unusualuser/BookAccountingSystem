package ru.example.exception;

public class BookHistoryRentAlreadyCompleted extends RuntimeException {
    public BookHistoryRentAlreadyCompleted(String message) {
        super(message);
    }
}
