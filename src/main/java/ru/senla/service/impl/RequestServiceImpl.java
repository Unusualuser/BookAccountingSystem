package ru.senla.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.exception.BookAvailableException;
import ru.senla.exception.BookNotFoundException;
import ru.senla.exception.InternalException;
import ru.senla.exception.RequestServiceOperationException;
import ru.senla.exception.UserNotFoundException;
import ru.senla.model.Book;
import ru.senla.model.Request;
import ru.senla.model.User;
import ru.senla.repository.RequestRepository;
import ru.senla.service.BookService;
import ru.senla.service.BookStorageService;
import ru.senla.service.RequestService;
import ru.senla.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    private final static Logger LOGGER = Logger.getLogger(RequestServiceImpl.class);
    private RequestRepository requestRepository;
    @Lazy
    @Autowired
    private BookService bookService;
    private UserService userService;
    private BookStorageService bookStorageService;

    public RequestServiceImpl(RequestRepository requestRepository,
                              UserService userService, BookStorageService bookStorageService) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.bookStorageService = bookStorageService;
    }

    @Transactional
    @Override
    public void requestBookByIdAndUserLogin(Long bookId, String userLogin) {
        try {
            boolean isBookAvailable = bookStorageService.getQuantityByBookId(bookId) > 0;

            if (isBookAvailable) {
                LOGGER.debug("Ошибка при создании запроса на книгу.");
                throw new BookAvailableException("Невозможно создать запрос на книгу с id %d, так как она в наличии");
            }

            Book bookToRequest = bookService.getBookById(bookId);
            User user = userService.getUserByLogin(userLogin);
            Request request = new Request(bookToRequest, user);

            requestRepository.saveRequest(request);
        } catch (BookNotFoundException e) {
            LOGGER.debug(String.format("%s %s", "Ошибка при создании запроса на книгу.", e.getMessage()), e);
            throw new RequestServiceOperationException("Книга с указанным bookId не найдена.");
        } catch (UserNotFoundException e) {
            LOGGER.debug(String.format("%s %s", "Ошибка при создании запроса на книгу.", e.getMessage()), e);
            throw new InternalException();
        }
    }

    @Transactional
    @Override
    public List<Request> closeBatchRequestsByBookIdAndBatch(Long bookId, Long batch) {
        bookService.throwBookNotFoundExceptionIfBookByIdNotContains(bookId, "Ошибка при закрытии запросов на книгу пачкой.");

        return requestRepository.closeBatchRequestsByBookIdAndBatch(bookId, batch);
    }

    @Transactional
    @Override
    public List<Request> getRequestsByBookIdForPeriod(Long id, LocalDateTime beginDttm, LocalDateTime endDttm) {
        bookService.throwBookNotFoundExceptionIfBookByIdNotContains(id, "Ошибка при получении запросов на книгу за период.");

        return requestRepository.getRequestsByBookIdForPeriod(id, beginDttm, endDttm);
    }

    @Transactional
    @Override
    public List<Request> getAllRequestsByBookId(Long bookId) {
        bookService.throwBookNotFoundExceptionIfBookByIdNotContains(bookId, "Ошибка при получении всех запросов на книгу.");

        return requestRepository.getAllRequestsByBookId(bookId);
    }

    @Transactional
    @Override
    public void deleteRequestsByBookId(Long bookId) {
        bookService.throwBookNotFoundExceptionIfBookByIdNotContains(bookId, "Ошибка при удалении всех запросов на книгу.");

        requestRepository.deleteRequestsByBookId(bookId);
    }
}
