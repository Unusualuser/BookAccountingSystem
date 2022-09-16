package ru.senla.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("Отказано в доступе. Сначала необходимо выполнить вход.");
    }
}
