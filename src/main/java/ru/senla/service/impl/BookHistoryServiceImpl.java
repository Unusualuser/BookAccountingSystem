package ru.senla.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.exception.BookHistoryNotFoundException;
import ru.senla.exception.BookHistoryServiceOperationException;
import ru.senla.exception.BookNotFoundException;
import ru.senla.exception.BookStorageIllegalReduceQuantityException;
import ru.senla.exception.BookStorageNotFoundException;
import ru.senla.exception.InternalException;
import ru.senla.exception.UserNotFoundException;
import ru.senla.model.Book;
import ru.senla.model.BookHistory;
import ru.senla.model.User;
import ru.senla.repository.BookHistoryRepository;
import ru.senla.service.BookHistoryService;
import ru.senla.service.BookService;
import ru.senla.service.BookStorageService;
import ru.senla.service.UserService;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookHistoryServiceImpl implements BookHistoryService {
    private final static Logger LOGGER = Logger.getLogger(BookHistoryServiceImpl.class);
    @Value("${book.rent.duration.in.month}")
    private int bookRentDurationInMonth;
    private BookHistoryRepository bookHistoryRepository;
    @Lazy
    @Autowired
    private BookService bookService;
    private UserService userService;
    private BookStorageService bookStorageService;

    public BookHistoryServiceImpl(BookHistoryRepository bookHistoryRepository,
                                  UserService userService,
                                  BookStorageService bookStorageService) {
        this.bookHistoryRepository = bookHistoryRepository;
        this.userService = userService;
        this.bookStorageService = bookStorageService;
    }

    @Transactional
    @Override
    public void rentBook(Long bookId, String userLogin) {
        try {
            bookStorageService.decrementQuantityByBookId(bookId);

            Book book = bookService.getBookById(bookId);
            User user = userService.getUserByLogin(userLogin);
            LocalDate rentalDate = LocalDate.now();
            LocalDate returnDeadlineDate = rentalDate.plusMonths(bookRentDurationInMonth);

            bookHistoryRepository.saveBookHistory(new BookHistory(book, user, rentalDate, returnDeadlineDate));
        } catch (BookStorageIllegalReduceQuantityException e) {
            LOGGER.debug(String.format("%s %s", "Ошибка при добавлении новой записи об аренде книги.", e.getMessage()), e);
            throw new BookHistoryServiceOperationException(e.getMessage());
        } catch (BookStorageNotFoundException e) {
            LOGGER.debug(String.format("%s %s", "Ошибка при добавлении новой записи об аренде книги.", e.getMessage()), e);
            throw new BookHistoryServiceOperationException(String.format("Книга с bookId %d не найдена в хранилище.", bookId));
        } catch (BookNotFoundException e) {
            LOGGER.debug(String.format("%s %s", "Ошибка при добавлении новой записи об аренде книги.", e.getMessage()), e);
            throw new BookHistoryServiceOperationException(String.format("Книга с bookId %d не найдена.", bookId));
        } catch (UserNotFoundException e) {
            LOGGER.debug(String.format("%s %s", "Ошибка при добавлении новой записи об аренде книги.", e.getMessage()), e);
            throw new InternalException();
        }
    }

    @Transactional
    @Override
    public void returnRentedBook(Long bookHistoryId) {
        try {
            LocalDate returnDate = LocalDate.now();
            BookHistory bookHistory = getBookHistoryById(bookHistoryId);
            bookHistory.setReturnDate(returnDate);

            bookStorageService.incrementQuantityByBookId(bookHistory.getBook().getId());
        } catch (BookHistoryNotFoundException e) {
            LOGGER.debug(String.format("%s %s", "Ошибка при возврате арендованной книги.", e.getMessage()), e);
            throw new BookHistoryNotFoundException(e.getMessage());
        } catch (BookStorageNotFoundException e) {
            LOGGER.debug(String.format("%s %s", "Ошибка при возврате арендованной книги.", e.getMessage()), e);
            throw new InternalException();
        }
    }

    @Transactional
    @Override
    public void deleteBookHistoriesByBookId(Long bookId) {
        bookService.throwBookNotFoundExceptionIfBookByIdNotContains(bookId, "Ошибка при удалении историй о книге.");

        bookHistoryRepository.deleteBookHistoriesByBookId(bookId);
    }

    @Transactional
    @Override
    public List<BookHistory> getFullBookHistoryByBookId(Long id) {
        bookService.throwBookNotFoundExceptionIfBookByIdNotContains(id, "Ошибка при получении полной истории о книге.");

        return bookHistoryRepository.getFullBookHistoryByBookId(id);
    }

    @Transactional
    @Override
    public List<BookHistory> getBookHistoriesByBookIdForPeriod(Long id, LocalDate beginDate, LocalDate endDate) {
        bookService.throwBookNotFoundExceptionIfBookByIdNotContains(id, "Ошибка при получении истории о книге за период.");

        return bookHistoryRepository.getBookHistoriesByBookIdForPeriod(id, beginDate, endDate);
    }

    @Override
    public BookHistory getBookHistoryById(Long id) {
        try {
            return bookHistoryRepository.getBookHistoryById(id);
        } catch (ObjectNotFoundException e) {
            String errorMessage = String.format("Запись истории аренды с id %d не найдена.", id);
            LOGGER.debug(String.format("%s %s", "Ошибка при получении истории книги.", e.getMessage()), e);
            LOGGER.error(errorMessage);
            throw new BookHistoryNotFoundException(errorMessage);
        }
    }

    @Override
    public List<BookHistory> findAndGetExpiredRent() {
        return bookHistoryRepository.findAndGetExpiredRent();
    }

    public void setBookRentDurationInMonth(int bookRentDurationInMonth) {
        this.bookRentDurationInMonth = bookRentDurationInMonth;
    }
}
