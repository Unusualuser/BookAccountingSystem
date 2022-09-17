package ru.senla.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.model.Book;
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
        Book book = bookService.getBookById(id);

        List<Request> batchRequests = requestService.closeBatchRequestsByBookIdAndBatch(id, additionalQuantity);
        bookStorageRepository.addQuantityByBookId(id, additionalQuantity);

        if (batchRequests.size() > 0) {
            List<User> usersToNotify = batchRequests.stream().map(Request::getUser).collect(Collectors.toList());
            String bookName = book.getName();
            String emailSubject = "Запрос на книгу в BookAccountingSystem";
            String emailMessage = String.format("Ваш запрос на книгу выполнен. Книга \"%s\" теперь в наличии", bookName);

            usersToNotify.forEach(user -> emailSender.sendMessage(user.getEmail(), emailSubject, emailMessage));
        }
    }

    @Override
    public void decrementQuantityByBookId(Long id) {
        bookStorageRepository.decrementQuantityByBookId(id);
    }

    @Override
    public void deleteBookStorageByBookId(Long bookId) {
        bookStorageRepository.deleteBookStorageByBookId(bookId);
    }

    @Transactional
    @Override
    public void reduceQuantityByBookId(Long id, Long quantityToBeReduce) {
        bookService.getBookById(id);

        bookStorageRepository.reduceQuantityByBookId(id, quantityToBeReduce);
    }

    @Transactional
    @Override
    public Long getQuantityByBookId(Long id) {
        bookService.getBookById(id);

        return bookStorageRepository.getQuantityByBookId(id);
    }
}
