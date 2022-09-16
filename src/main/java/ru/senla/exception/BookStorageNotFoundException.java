package ru.senla.exception;

public class BookStorageNotFoundException extends RuntimeException {
    public BookStorageNotFoundException(String message) {
        super(message);
    }
}
