package ru.senla.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.exception.BookHistoryServiceOperationException;
import ru.senla.model.Book;
import ru.senla.model.BookHistory;
import ru.senla.model.User;
import ru.senla.repository.BookHistoryRepository;
import ru.senla.repository.BookRepository;
import ru.senla.repository.BookStorageRepository;
import ru.senla.repository.UserRepository;
import ru.senla.service.BookHistoryService;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookHistoryServiceImpl implements BookHistoryService {
    private final static Logger LOGGER = Logger.getLogger(BookHistoryServiceImpl.class);
    @Value("${book.rent.duration.in.month}")
    private int bookRentDurationInMonth;
    private BookHistoryRepository bookHistoryRepository;
    private BookRepository bookRepository;
    private UserRepository userRepository;
    private BookStorageRepository bookStorageRepository;

    public BookHistoryServiceImpl(BookHistoryRepository bookHistoryRepository, BookRepository bookRepository, UserRepository userRepository, BookStorageRepository bookStorageRepository) {
        this.bookHistoryRepository = bookHistoryRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.bookStorageRepository = bookStorageRepository;
    }

    @Transactional
    @Override
    public void rentBook(Long bookId, String userLogin) {
        try {
            this.bookStorageRepository.decrementQuantityByBookId(bookId);

            Book book = this.bookRepository.getBookById(bookId);
            User user = this.userRepository.getUserByLogin(userLogin);
            LocalDate rentalDate = LocalDate.now();
            LocalDate returnDeadlineDate = rentalDate.plusMonths(this.bookRentDurationInMonth);

            this.bookHistoryRepository.saveBookHistory(new BookHistory(book, user, rentalDate, returnDeadlineDate));
        } catch (RuntimeException e) {
            String errorMessage = "Ошибка при добавлении новой записи об аренде книги.";
            LOGGER.error(String.format("%s %s", errorMessage, e.getMessage()), e);
            LOGGER.debug(String.format("Id книги: %d, логин пользователя: %s.", bookId, userLogin));
            throw new BookHistoryServiceOperationException(errorMessage, e);
        }
    }

    @Transactional
    @Override
    public void returnRentedBook(Long bookHistoryId) {
        try {
            LocalDate returnDate = LocalDate.now();

            BookHistory bookHistory = getBookHistoryById(bookHistoryId);
            bookHistory.setReturnDate(returnDate);

            this.bookStorageRepository.incrementQuantityByBookId(bookHistory.getBook().getId());
        } catch (RuntimeException e) {
            String errorMessage = "Ошибка при установке даты возврата.";
            LOGGER.error(String.format("%s %s", errorMessage, e.getMessage()), e);
            LOGGER.debug(String.format("Id записи истории книги: %d.", bookHistoryId));
            throw new BookHistoryServiceOperationException(errorMessage, e);
        }
    }

    @Override
    public List<BookHistory> getFullBookHistoryByBookId(Long id) {
        try {
            return this.bookHistoryRepository.getFullBookHistoryByBookId(id);
        } catch (RuntimeException e) {
            String errorMessage = "Ошибка при получении полной истории книги.";
            LOGGER.error(String.format("%s %s", errorMessage, e.getMessage()), e);
            LOGGER.debug(String.format("Id книги: %d.", id));
            throw new BookHistoryServiceOperationException(errorMessage, e);
        }
    }

    @Override
    public List<BookHistory> getBookHistoriesByBookIdForPeriod(Long id, LocalDate beginDate, LocalDate endDate) {
        try {
            return this.bookHistoryRepository.getBookHistoriesByBookIdForPeriod(id, beginDate, endDate);
        } catch (RuntimeException e) {
            String errorMessage = "Ошибка при получении истории книги за период.";
            LOGGER.error(String.format("%s %s", errorMessage, e.getMessage()), e);
            if (beginDate != null && endDate != null)
                LOGGER.debug(String.format("Id книги: %d, дата начала периода: %s, дата конца периода: %s.", id, beginDate.toString(), endDate.toString()));
            throw new BookHistoryServiceOperationException(errorMessage, e);
        }
    }

    @Override
    public BookHistory getBookHistoryById(Long id) {
        try {
            return this.bookHistoryRepository.getBookHistoryById(id);
        } catch (RuntimeException e) {
            String errorMessage = "Ошибка при получении истории книги.";
            LOGGER.error(String.format("%s %s", errorMessage, e.getMessage()), e);
            LOGGER.debug(String.format("Id истории книги: %d.", id));
            throw new BookHistoryServiceOperationException(errorMessage, e);
        }
    }

    @Override
    public List<BookHistory> findAndGetExpiredRent() {
        try {
            return this.bookHistoryRepository.findAndGetExpiredRent();
        } catch (RuntimeException e) {
            String errorMessage = "Ошибка при получении просроченных аренд книг.";
            LOGGER.error(String.format("%s %s", errorMessage, e.getMessage()), e);
            throw new BookHistoryServiceOperationException(errorMessage, e);
        }
    }

    public void setBookRentDurationInMonth(int bookRentDurationInMonth) {
        this.bookRentDurationInMonth = bookRentDurationInMonth;
    }
}
