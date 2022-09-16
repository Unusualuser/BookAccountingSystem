package ru.senla.exception;

public class BookHistoryRentAlreadyCompleted extends RuntimeException {
    public BookHistoryRentAlreadyCompleted(String message) {
        super(message);
    }
}
