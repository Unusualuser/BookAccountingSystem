package ru.example.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.exception.BookAvailableException;
import ru.example.model.Book;
import ru.example.model.Request;
import ru.example.model.User;
import ru.example.repository.RequestRepository;
import ru.example.service.BookService;
import ru.example.service.BookStorageService;
import ru.example.service.RequestService;
import ru.example.service.UserService;

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
            Book bookToRequest = bookService.getBookById(bookId);

            boolean isBookAvailable = bookStorageService.getQuantityByBookId(bookId) > 0;

            if (isBookAvailable) {
                LOGGER.debug("Ошибка при создании запроса на книгу.");
                throw new BookAvailableException("Невозможно создать запрос на книгу с id %d, так как она в наличии");
            }

            User user = userService.getUserByLogin(userLogin);
            Request request = new Request(bookToRequest, user);

            requestRepository.saveRequest(request);
    }

    @Override
    public List<Request> closeBatchRequestsByBookIdAndBatch(Long bookId, Long batch) {
        return requestRepository.closeBatchRequestsByBookIdAndBatch(bookId, batch);
    }

    @Transactional
    @Override
    public List<Request> getRequestsByBookIdForPeriod(Long id, LocalDateTime beginDttm, LocalDateTime endDttm) {
        bookService.getBookById(id);

        return requestRepository.getRequestsByBookIdForPeriod(id, beginDttm, endDttm);
    }

    @Transactional
    @Override
    public List<Request> getAllRequestsByBookId(Long bookId) {
        bookService.getBookById(bookId);

        return requestRepository.getAllRequestsByBookId(bookId);
    }

    @Transactional
    @Override
    public void deleteRequestsByBookId(Long bookId) {
        requestRepository.deleteRequestsByBookId(bookId);
    }
}
