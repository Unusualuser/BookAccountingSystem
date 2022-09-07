package ru.senla.exception;

public class UserServiceOperationException extends RuntimeException {
    public UserServiceOperationException(String message) {
        super(message);
    }

    public UserServiceOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
