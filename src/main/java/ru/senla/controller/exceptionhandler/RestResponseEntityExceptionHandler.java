package ru.senla.controller.exceptionhandler;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.senla.exception.BookAvailableException;
import ru.senla.exception.BookHistoryNotFoundException;
import ru.senla.exception.BookHistoryRentAlreadyCompleted;
import ru.senla.exception.BookNotFoundException;
import ru.senla.exception.BookStorageIllegalReduceQuantityException;
import ru.senla.exception.BookStorageNotFoundException;
import ru.senla.exception.UserAlreadyExistException;
import ru.senla.exception.UserNotFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private final static Logger LOGGER = Logger.getLogger(RestResponseEntityExceptionHandler.class);

    private ResponseEntity<Object> handleErrorMessage(Exception e,
                                                      WebRequest request,
                                                      String errorMessage,
                                                      HttpStatus httpStatus) {
        return handleExceptionInternal(e, Collections.singletonMap("error", errorMessage), new HttpHeaders(), httpStatus, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String errorMessage = String.format("Некорректные входные данные. %s",
                                            e.getBindingResult().getFieldErrors().stream()
                                                                                 .map(FieldError::getDefaultMessage)
                                                                                 .collect(Collectors.toList()));

        return handleErrorMessage(e, request, errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e,
                                                                        WebRequest request) {
        String errorMessage = String.format("Некорректные входные данные. %s",
                                            e.getConstraintViolations().stream()
                                                                       .map(ConstraintViolation::getMessage)
                                                                       .collect(Collectors.toList()));

        return handleErrorMessage(e, request, errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    protected ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException e, WebRequest request) {
        String errorMessage = String.format("Ошибка аутентификации. %s", e.getMessage());

        return handleErrorMessage(e, request, errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BookHistoryNotFoundException.class,
                               BookNotFoundException.class,
                               BookStorageNotFoundException.class,
                               UserNotFoundException.class})
    protected ResponseEntity<Object> handleNotFoundExceptions(RuntimeException e, WebRequest request) {
        String errorMessage = e.getMessage();

        return handleErrorMessage(e, request, errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {BookStorageIllegalReduceQuantityException.class,
                               UserAlreadyExistException.class,
                               BookAvailableException.class,
                               BookHistoryRentAlreadyCompleted.class})
    protected ResponseEntity<Object> handleBadRequestExceptions(RuntimeException e, WebRequest request) {
        String errorMessage = e.getMessage();

        return handleErrorMessage(e, request, errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<Object> handleInternalExceptions(RuntimeException e,
                                                              WebRequest request) {
        String errorMessage = "Внутренняя ошибка.";

        LOGGER.debug(String.format("%s %s", errorMessage, e.getMessage()), e);

        return handleErrorMessage(e, request, errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
