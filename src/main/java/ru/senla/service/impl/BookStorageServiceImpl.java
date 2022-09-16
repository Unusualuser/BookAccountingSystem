package ru.senla.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.exception.BookStorageIllegalReduceQuantityException;
import ru.senla.exception.BookStorageNotFoundException;
import ru.senla.exception.EmailSendingException;
import ru.senla.exception.InternalException;
import ru.senla.model.Request;
import ru.senla.model.User;
import ru.senla.repository.BookStorageRepository;
import ru.senla.service.BookService;
import ru.senla.service.BookStorageService;
import ru.senla.service.RequestService;
import ru.senla.util.EmailSender;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookStorageServiceImpl implements BookStorageService {
    private final static Logger LOGGER = Logger.getLogger(BookStorageServiceImpl.class);
    private BookStorageRepository bookStorageRepository;
    @Lazy
    @Autowired
    private RequestService requestService;
    @Lazy
    @Autowired
    private BookService bookService;
    private EmailSender emailSender;

    public BookStorageServiceImpl(BookStorageRepository bookStorageRepository,
                                  EmailSender emailSender) {
        this.bookStorageRepository = bookStorageRepository;
        this.emailSender = emailSender;
    }

    @Transactional
    @Override
    public void addQuantityByBookIdCloseRequestsIfExistsAndNotifyUsers(Long id, Long additionalQuantity) {
        bookService.throwBookNotFoundExceptionIfBookByIdNotContains(id, "Ошибка при добавлении количества книг в хранилище и закрытии запросов.");

        try {
            List<Request> batchRequests = requestService.closeBatchRequestsByBookIdAndBatch(id, additionalQuantity);
            bookStorageRepository.addQuantityByBookId(id, additionalQuantity);

            if (batchRequests.size() > 0) {
                List<User> usersToNotify = batchRequests.stream().map(Request::getUser).collect(Collectors.toList());
                String bookName = batchRequests.get(0).getBook().getName();
                String emailSubject = "Запрос на книгу в BookAccountingSystem";
                String emailMessage = String.format("Ваш запрос на книгу выполнен. Книга \"%s\" теперь в наличии", bookName);

                usersToNotify.forEach(user -> emailSender.sendMessage(user.getEmail(), emailSubject, emailMessage));
            }
        } catch (ObjectNotFoundException e) {
            String errorMessage = String.format("Книга с id %d не найдена в хранилище.", id);
            LOGGER.debug(String.format("%s %s", "Ошибка при добавлении количества книг в хранилище.", e.getMessage()), e);
            LOGGER.error(errorMessage);
            throw new BookStorageNotFoundException(errorMessage);
        } catch (EmailSendingException e) {
            LOGGER.debug(String.format("%s %s", "Ошибка при добавлении количества книг в хранилище.", e.getMessage()), e);
            throw new InternalException();
        }
    }

    @Transactional
    @Override
    public void incrementQuantityByBookId(Long id) {
        bookService.throwBookNotFoundExceptionIfBookByIdNotContains(id, "Ошибка при инкременте количества книг в хранилище.");

        try {
            bookStorageRepository.incrementQuantityByBookId(id);
        } catch (ObjectNotFoundException e) {
            String errorMessage = String.format("Книга с id %d не найдена в хранилище.", id);
            LOGGER.debug(String.format("%s %s", "Ошибка при добавлении количества книг в хранилище.", e.getMessage()), e);
            LOGGER.error(errorMessage);
            throw new BookStorageNotFoundException(errorMessage);
        }
    }

    @Transactional
    @Override
    public void decrementQuantityByBookId(Long id) {
        bookService.throwBookNotFoundExceptionIfBookByIdNotContains(id, "Ошибка при декременте количества книг в хранилище.");

        try {
            bookStorageRepository.decrementQuantityByBookId(id);
        } catch (BookStorageIllegalReduceQuantityException e) {
            LOGGER.debug(String.format("%s %s", "Ошибка при убавлении количества книг в хранилище.", e.getMessage()), e);
            throw new BookStorageIllegalReduceQuantityException(id);
        } catch (ObjectNotFoundException e) {
            String errorMessage = String.format("Книга с id %d не найдена в хранилище.", id);
            LOGGER.debug(String.format("%s %s", "Ошибка при убавлении количества книг в хранилище.", e.getMessage()), e);
            LOGGER.error(errorMessage);
            throw new BookStorageNotFoundException(errorMessage);
        }
    }

    @Transactional
    @Override
    public void deleteBookStorageByBookId(Long bookId) {
        bookService.throwBookNotFoundExceptionIfBookByIdNotContains(bookId, "Ошибка при удалении книги из хранилища.");

        bookStorageRepository.deleteBookStorageByBookId(bookId);
    }

    @Transactional
    @Override
    public void reduceQuantityByBookId(Long id, Long quantityToBeReduce) {
        bookService.throwBookNotFoundExceptionIfBookByIdNotContains(id, "Ошибка при уменьшении количества книг в хранилище.");

        try {
            bookStorageRepository.reduceQuantityByBookId(id, quantityToBeReduce);
        } catch (BookStorageIllegalReduceQuantityException e) {
            LOGGER.debug(String.format("%s %s", "Ошибка при убавлении количества книг в хранилище.", e.getMessage()), e);
            throw new BookStorageIllegalReduceQuantityException(id);
        } catch (ObjectNotFoundException e) {
            String errorMessage = String.format("Книга с id %d не найдена в хранилище.", id);
            LOGGER.debug(String.format("%s %s", "Ошибка при убавлении количества книг в хранилище.", e.getMessage()), e);
            LOGGER.error(errorMessage);
            throw new BookStorageNotFoundException(errorMessage);
        }
    }

    @Transactional
    @Override
    public Long getQuantityByBookId(Long id) {
        bookService.throwBookNotFoundExceptionIfBookByIdNotContains(id, "Ошибка при получении количества книг в хранилище.");

        try {
            return bookStorageRepository.getQuantityByBookId(id);
        } catch (ObjectNotFoundException e) {
            String errorMessage = String.format("Книга с id %d не найдена в хранилище.", id);
            LOGGER.debug(String.format("%s %s", "Ошибка при получении количества книг в хранилище.", e.getMessage()), e);
            LOGGER.error(errorMessage);
            throw new BookStorageNotFoundException(errorMessage);
        }
    }
}
