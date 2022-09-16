package ru.senla.service;

import ru.senla.model.BookHistory;

import java.time.LocalDate;
import java.util.List;

public interface BookHistoryService {
    void rentBook(Long bookId, String userLogin);

    void returnRentedBook(Long bookHistoryId);

    void deleteBookHistoriesByBookId(Long bookId);

    List<BookHistory> getFullBookHistoryByBookId(Long id);

    List<BookHistory> getBookHistoriesByBookIdForPeriod(Long id, LocalDate beginDate, LocalDate endDate);

    BookHistory getBookHistoryById(Long id);

    List<BookHistory> findAndGetExpiredRent();
}
