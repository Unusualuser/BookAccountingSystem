package ru.senla.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String login) {
        super(String.format("Пользователь с логином %s уже зарегистрирован", login));
    }
}
