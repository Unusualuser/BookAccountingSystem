package ru.example.exception;

public class EmailSendingException extends RuntimeException {
    public EmailSendingException(String message) {
        super(message);
    }
}
