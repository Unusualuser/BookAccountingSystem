package ru.example.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.example.exception.BookHistoryNotFoundException;
import ru.example.model.Book;
import ru.example.model.BookHistory;
import ru.example.model.User;
import ru.example.repository.BookHistoryRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@SqlGroup({
        @Sql(value = "classpath:dbscripts/truncateTables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "classpath:dbscripts/insertAndUpdate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
class BookHistoryRepositoryImplTest {
    @Autowired
    BookHistoryRepository bookHistoryRepository;

    @DisplayName("Integration positive test for BookHistoryRepositoryImplTest saveBookHistory method")
    @Test
    void givenBookHistory_whenSaveBookHistoryInvoked_thenBookHistorySaved() {
        // arrange
        Book book = new Book(1L, null, null, null, null);
        User user = new User(null, null, null, 1L, null, null, null, null);
        LocalDate rentalDate = LocalDate.now();
        LocalDate returnDeadlineDate = LocalDate.now().plusMonths(2);
        BookHistory bookHistoryToSave = new BookHistory(book, user, rentalDate, returnDeadlineDate);

        // act
        bookHistoryRepository.saveBookHistory(bookHistoryToSave);

        // assert
        BookHistory savedBookHistory = bookHistoryRepository.getBookHistoryById(7L);
        assertEquals(book.getId(), savedBookHistory.getBook().getId());
        assertEquals(user.getId(), savedBookHistory.getUser().getId());
        assertEquals(rentalDate, savedBookHistory.getRentalDate());
        assertEquals(returnDeadlineDate, savedBookHistory.getReturnDeadlineDate());
    }

    @DisplayName("Integration negative test for BookHistoryRepositoryImplTest saveBookHistory method")
    @Test
    void givenBookHistoryWithNonexistentBook_whenSaveBookHistoryInvoked_thenDataIntegrityViolationExceptionThrown() {
        // arrange
        Book book = new Book(9999L, null, null, null, null);
        User user = new User(null, null, null, 1L, null, null, null, null);
        LocalDate rentalDate = LocalDate.now();
        LocalDate returnDeadlineDate = LocalDate.now().plusMonths(2);
        BookHistory bookHistoryToSave = new BookHistory(book, user, rentalDate, returnDeadlineDate);

        // act and assert
        assertThrows(DataIntegrityViolationException.class, () -> bookHistoryRepository.saveBookHistory(bookHistoryToSave));
    }

    @DisplayName("Integration positive test for BookHistoryRepositoryImplTest getFullBookHistoryByBookId method")
    @Test
    void givenBookId_whenGetFullBookHistoryByBookIdInvoked_thenFullBookHistoryByBookIdReturned() {
        // arrange
        Long bookId = 1L;

        // act
        List<BookHistory> fullBookHistoryByBookId = bookHistoryRepository.getFullBookHistoryByBookId(bookId);

        // assert
        assertEquals(3, fullBookHistoryByBookId.size());
    }

    @DisplayName("Integration negative test for BookHistoryRepositoryImplTest getFullBookHistoryByBookId method")
    @Test
    void givenNonexistentBookId_whenGetFullBookHistoryByBookIdInvoked_thenZeroBookHistoriesReturned() {
        // arrange
        Long nonexistentBookId = 9999L;

        // act
        List<BookHistory> fullBookHistoryByBookId = bookHistoryRepository.getFullBookHistoryByBookId(nonexistentBookId);

        // assert
        assertEquals(0, fullBookHistoryByBookId.size());
    }

    @DisplayName("Integration positive test for BookHistoryRepositoryImplTest getBookHistoriesByBookIdForPeriod method")
    @Test
    void givenBookIdAndPeriod_whenGetBookHistoriesByBookIdForPeriodInvoked_thenBookHistoriesByBookIdForPeriodReturned() {
        // arrange
        Long bookId = 1L;
        LocalDate beginDate = LocalDate.of(2022, 6, 1);
        LocalDate endDate = LocalDate.of(2022, 8, 1);

        // act
        List<BookHistory> bookHistoriesForPeriod = bookHistoryRepository.getBookHistoriesByBookIdForPeriod(bookId, beginDate, endDate);

        // assert
        assertEquals(2, bookHistoriesForPeriod.size());
    }

    @DisplayName("Integration negative test for BookHistoryRepositoryImplTest getBookHistoriesByBookIdForPeriod method")
    @Test
    void givenNonexistentBookIdAndPeriod_whenGetBookHistoriesByBookIdForPeriodInvoked_thenEmptyListWithBookHistoriesReturned() {
        // arrange
        Long nonexistentBookId = 9999L;
        LocalDate beginDate = LocalDate.of(2022, 6, 1);
        LocalDate endDate = LocalDate.of(2022, 8, 1);

        // act
        List<BookHistory> bookHistoriesForPeriod = bookHistoryRepository.getBookHistoriesByBookIdForPeriod(nonexistentBookId, beginDate, endDate);

        // assert
        assertEquals(0, bookHistoriesForPeriod.size());
    }

    @DisplayName("Integration positive test for BookHistoryRepositoryImplTest getBookHistoryById method")
    @Test
    void givenBookHistoryId_whenGetBookHistoryByIdInvoked_thenBookHistoryReturned() {
        // arrange
        Long bookHistoryId = 1L;

        // act
        BookHistory bookHistory = bookHistoryRepository.getBookHistoryById(bookHistoryId);

        // assert
        assertEquals(bookHistoryId, bookHistory.getId());
        assertEquals(1, bookHistory.getBook().getId());
        assertEquals(1, bookHistory.getUser().getId());
    }

    @DisplayName("Integration negative test for BookHistoryRepositoryImplTest getBookHistoryById method")
    @Test
    void givenNonexistentBookHistoryId_whenGetBookHistoryByIdInvoked_thenBookHistoryNotFoundExceptionThrown() {
        // arrange
        Long nonexistentBookHistoryId = 9999L;

        // act and assert
        assertThrows(BookHistoryNotFoundException.class, () -> bookHistoryRepository.getBookHistoryById(nonexistentBookHistoryId));
    }

    @DisplayName("Integration positive test for BookHistoryRepositoryImplTest deleteBookHistoriesByBookId method")
    @Test
    void givenBookId_whenDeleteBookHistoriesByBookIdInvoked_thenBookHistoriesDeleted() {
        // arrange
        Long bookId = 1L;

        // act
       bookHistoryRepository.deleteBookHistoriesByBookId(bookId);

        // assert
        assertEquals(0, bookHistoryRepository.getFullBookHistoryByBookId(bookId).size());
    }

    @DisplayName("Integration negative test for BookHistoryRepositoryImplTest deleteBookHistoriesByBookId method")
    @Test
    void givenNonexistentBookId_whenDeleteBookHistoriesByBookIdInvoked_thenNothing() {
        // arrange
        Long nonexistentBookId = 9999L;

        // act
        bookHistoryRepository.deleteBookHistoriesByBookId(nonexistentBookId);
    }

    @DisplayName("Integration positive test for BookHistoryRepositoryImplTest findAndGetExpiredRent method")
    @Test
    void whenFindAndGetExpiredRentInvoked_thenExpiredRentReturned() {
        // act
        List<BookHistory> expiredRents = bookHistoryRepository.findAndGetExpiredRent();

        // assert
        assertEquals(1, expiredRents.size());
    }
}