package ru.senla.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.senla.exception.BookStorageServiceOperationException;
import ru.senla.model.Request;
import ru.senla.model.User;
import ru.senla.repository.BookStorageRepository;
import ru.senla.repository.RequestRepository;
import ru.senla.service.BookStorageService;
import ru.senla.util.EmailSender;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookStorageServiceImpl implements BookStorageService {
    private final static Logger LOGGER = Logger.getLogger(BookStorageServiceImpl.class);
    private final BookStorageRepository bookStorageRepository;
    private final RequestRepository requestRepository;
    private final EmailSender emailSender;

    public BookStorageServiceImpl(BookStorageRepository bookStorageRepository,
                                  RequestRepository requestRepository,
                                  EmailSender emailSender) {
        this.bookStorageRepository = bookStorageRepository;
        this.requestRepository = requestRepository;
        this.emailSender = emailSender;
    }

    @Override
    public void addQuantityByBookIdCloseRequestsIfExistsAndNotifyUsers(Long id, Long additionalQuantity) {
        try {
            List<Request> batchRequests = this.requestRepository.closeBatchRequestsByBookIdAndBatch(id, additionalQuantity);
            this.bookStorageRepository.addQuantityByBookId(id, additionalQuantity);

            if (batchRequests.size() > 0) {
                List<User> usersToNotify = batchRequests.stream().map(Request::getUser).collect(Collectors.toList());
                String bookName = batchRequests.get(0).getBook().getName();
                String emailSubject = "Запрос на книгу в BookAccountingSystem";
                String emailMessage = String.format("Ваш запрос на книгу выполнен. Книга \"%s\" теперь в наличии", bookName);

                usersToNotify.forEach(user -> this.emailSender.sendMessage(user.getEmail(), emailSubject, emailMessage));
            }
        } catch (Exception e) {
            String errorMessage = "Ошибка при добавлении количества книг в хранилище.";
            LOGGER.error(String.format("%s %s", errorMessage, e.getMessage()), e);
            LOGGER.debug(String.format("Id книги: %d.", id));
            throw new BookStorageServiceOperationException(errorMessage, e);
        }
    }

    @Override
    public void reduceQuantityByBookId(Long id, Long quantityToBeReduce) {
        try {
            this.bookStorageRepository.reduceQuantityByBookId(id, quantityToBeReduce);
        } catch (Exception e) {
            String errorMessage = "Ошибка при убавлении количества книг в хранилище.";
            LOGGER.error(String.format("%s %s", errorMessage, e.getMessage()), e);
            LOGGER.debug(String.format("Id книги: %d.", id));
            throw new BookStorageServiceOperationException(errorMessage, e);
        }
    }

    @Override
    public Long getQuantityByBookId(Long id) {
        try {
            return this.bookStorageRepository.getQuantityByBookId(id);
        } catch (Exception e) {
            String errorMessage = "Ошибка при извлечении книг из хранилища.";
            LOGGER.error(String.format("%s %s", errorMessage, e.getMessage()), e);
            LOGGER.debug(String.format("Id книги: %d.", id));
            throw new BookStorageServiceOperationException(errorMessage, e);
        }
    }
}
