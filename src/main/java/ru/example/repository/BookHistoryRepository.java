package ru.example.repository;

import ru.example.model.BookHistory;

import java.time.LocalDate;
import java.util.List;

public interface BookHistoryRepository {
    void saveBookHistory(BookHistory bookHistory);

    List<BookHistory> getFullBookHistoryByBookId(Long id);

    List<BookHistory> getBookHistoriesByBookIdForPeriod(Long id, LocalDate beginDate, LocalDate endDate);

    BookHistory getBookHistoryById(Long id);

    void deleteBookHistoriesByBookId(Long bookId);

    List<BookHistory> findAndGetExpiredRent();
}
