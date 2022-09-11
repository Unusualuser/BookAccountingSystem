package ru.senla.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.exception.RequestServiceOperationException;
import ru.senla.model.Book;
import ru.senla.model.Request;
import ru.senla.model.User;
import ru.senla.repository.BookRepository;
import ru.senla.repository.RequestRepository;
import ru.senla.repository.UserRepository;
import ru.senla.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    private final static Logger LOGGER = Logger.getLogger(RequestServiceImpl.class);
    private RequestRepository requestRepository;
    private BookRepository bookRepository;
    private UserRepository userRepository;

    public RequestServiceImpl(RequestRepository requestRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void requestBookByIdAndUserLogin(Long bookId, String userLogin) {
        try {
            Book bookToRequest = this.bookRepository.getBookById(bookId);
            User user = this.userRepository.getUserByLogin(userLogin);
            Request request = new Request(bookToRequest, user);

            this.requestRepository.saveRequest(request);
        } catch (RuntimeException e) {
            String errorMessage = "Ошибка при создании запроса на книгу.";
            LOGGER.error(String.format("%s %s", errorMessage, e.getMessage()), e);
            LOGGER.debug(String.format("Id запрашиваемой книги: %d, логин пользователя: %s.", bookId, userLogin));
            throw new RequestServiceOperationException(errorMessage, e);
        }
    }

    @Override
    public List<Request> getRequestsByBookIdForPeriod(Long id, LocalDateTime beginDttm, LocalDateTime endDttm) {
        try {
            return this.requestRepository.getRequestsByBookIdForPeriod(id, beginDttm, endDttm);
        } catch (RuntimeException e) {
            String errorMessage = "Ошибка при получении запросов на книгу за период.";
            LOGGER.error(String.format("%s %s", errorMessage, e.getMessage()), e);
            if (beginDttm != null && endDttm != null)
                LOGGER.debug(String.format("Id книги: %d, начало периода: %s, конец периода: %s.", id, beginDttm.toString(), endDttm.toString()));
            throw new RequestServiceOperationException(errorMessage, e);
        }
    }

    @Override
    public List<Request> getAllRequestsByBookId(Long bookId) {
        try {
        return this.requestRepository.getAllRequestsByBookId(bookId);
        } catch (RuntimeException e) {
            String errorMessage = "Ошибка при получении всех запросов на книгу.";
            LOGGER.error(String.format("%s %s", errorMessage, e.getMessage()), e);
            LOGGER.debug(String.format("Id книги: %d.", bookId));
            throw new RequestServiceOperationException(errorMessage, e);
        }
    }
}
