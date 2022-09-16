package ru.senla.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.exception.BookHistoryRentAlreadyCompleted;
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
        Book book = bookService.getBookById(bookId);
        User user = userService.getUserByLogin(userLogin);
        LocalDate rentalDate = LocalDate.now();
        LocalDate returnDeadlineDate = rentalDate.plusMonths(bookRentDurationInMonth);

        bookStorageService.decrementQuantityByBookId(bookId);

        bookHistoryRepository.saveBookHistory(new BookHistory(book, user, rentalDate, returnDeadlineDate));
    }

    @Transactional
    @Override
    public void returnRentedBook(Long bookHistoryId) {
        BookHistory bookHistory = getBookHistoryById(bookHistoryId);

        if (bookHistory.getReturnDate() != null) {
            String errorMessage = String.format("Невозможно завершить аренду книги, так как она уже завершена для bookHistoryId %d", bookHistoryId);
            LOGGER.error(errorMessage);
            throw new BookHistoryRentAlreadyCompleted(errorMessage);
        }

        LocalDate returnDate = LocalDate.now();
        bookHistory.setReturnDate(returnDate);

        bookStorageService.incrementQuantityByBookId(bookHistory.getBook().getId());
    }

    @Override
    public void deleteBookHistoriesByBookId(Long bookId) {
        bookHistoryRepository.deleteBookHistoriesByBookId(bookId);
    }

    @Transactional
    @Override
    public List<BookHistory> getFullBookHistoryByBookId(Long id) {
        bookService.getBookById(id);

        return bookHistoryRepository.getFullBookHistoryByBookId(id);
    }

    @Transactional
    @Override
    public List<BookHistory> getBookHistoriesByBookIdForPeriod(Long id, LocalDate beginDate, LocalDate endDate) {
        bookService.getBookById(id);

        return bookHistoryRepository.getBookHistoriesByBookIdForPeriod(id, beginDate, endDate);
    }

    @Override
    public BookHistory getBookHistoryById(Long id) {
        return bookHistoryRepository.getBookHistoryById(id);
    }

    @Override
    public List<BookHistory> findAndGetExpiredRent() {
        return bookHistoryRepository.findAndGetExpiredRent();
    }

    public void setBookRentDurationInMonth(int bookRentDurationInMonth) {
        this.bookRentDurationInMonth = bookRentDurationInMonth;
    }
}
