package ru.senla.controller.exceptionhandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.senla.exception.BookHistoryServiceOperationException;
import ru.senla.exception.BookServiceOperationException;
import ru.senla.exception.BookStorageServiceOperationException;
import ru.senla.exception.RequestServiceOperationException;
import ru.senla.exception.UserServiceOperationException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private ResponseEntity<Object> handleErrorMessage(Exception e, WebRequest request, String errorMessage, HttpStatus httpStatus) {
        logger.error(errorMessage);
        return handleExceptionInternal(e,
                Collections.singletonMap("error", errorMessage),
                new HttpHeaders(),
                httpStatus,
                request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        e.getMessage();
        String errorMessage = String.format("Некорректные входные данные. %s",
                e.getBindingResult().getFieldErrors().stream()
                                                     .map(FieldError::getDefaultMessage)
                                                     .collect(Collectors.toList()));
        return handleErrorMessage(e, request, errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e, WebRequest request) {
        String errorMessage = String.format("Некорректные входные данные. %s",
                e.getConstraintViolations().stream()
                                           .map(ConstraintViolation::getMessage)
                                           .collect(Collectors.toList()));
        return handleErrorMessage(e, request, errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BookHistoryServiceOperationException.class,
                               BookServiceOperationException.class,
                               BookStorageServiceOperationException.class,
                               RequestServiceOperationException.class, UserServiceOperationException.class})
    protected ResponseEntity<Object> handleServicesOperationsExceptions(RuntimeException e, WebRequest request) {
        String errorMessage = String.format("Ошибка выполнения операции. %s",
                e.getMessage() != null ? e.getMessage() : "");
        return handleErrorMessage(e, request, errorMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleInternalExceptions(Exception e, WebRequest request) {
        String errorMessage = "Внутренняя ошибка.";
        return handleErrorMessage(e, request, errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
