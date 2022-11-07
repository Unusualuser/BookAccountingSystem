package ru.example.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.example.exception.BookStorageIllegalReduceQuantityException;
import ru.example.exception.BookStorageNotFoundException;
import ru.example.model.BookStorage;
import ru.example.repository.BookStorageRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@SqlGroup({
        @Sql(value = "classpath:dbscripts/truncateTables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "classpath:dbscripts/insertAndUpdate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
class BookStorageRepositoryImplTest {
    @Autowired
    BookStorageRepository bookStorageRepository;

    @DisplayName("Integration positive test for BookStorageRepositoryImplTest addQuantityByBookId method")
    @Test
    void givenBookId_whenAddQuantityByBookIdInvoked_thenBookReturned() {
        // arrange
        Long bookId = 1L;

        // act
        bookStorageRepository.addQuantityByBookId(bookId, 1L);

        // assert
        assertEquals(4, bookStorageRepository.getQuantityByBookId(bookId));
    }

    @DisplayName("Integration negative test for BookStorageRepositoryImplTest addQuantityByBookId method")
    @Test
    void givenNonexistentBookId_whenAddQuantityByBookIdInvoked_thenBookStorageNotFoundExceptionThrown() {
        // arrange
        Long nonexistentBookId = 9999L;

        // act and assert
        assertThrows(BookStorageNotFoundException.class, () -> bookStorageRepository.addQuantityByBookId(nonexistentBookId, 1L));
    }

    @DisplayName("Integration positive test for BookStorageRepositoryImplTest reduceQuantityByBookId method")
    @Test
    void givenBookId_whenReduceQuantityByBookIdInvoked_thenBookQuantityReduced() {
        // arrange
        Long bookId = 1L;

        // act
        bookStorageRepository.reduceQuantityByBookId(bookId, 1L);

        // assert
        assertEquals(2, bookStorageRepository.getQuantityByBookId(bookId));
    }

    @DisplayName("Integration negative test for BookStorageRepositoryImplTest reduceQuantityByBookId method")
    @Test
    void givenBookIdWithZeroQuantityInBookStorage_whenReduceQuantityByBookIdInvoked_thenBookStorageIllegalReduceQuantityExceptionThrown() {
        // arrange
        Long bookIdWithZeroQuantity = 4L;

        // act and assert
        assertThrows(BookStorageIllegalReduceQuantityException.class, () -> bookStorageRepository.reduceQuantityByBookId(bookIdWithZeroQuantity, 1L));
    }

    @DisplayName("Integration positive test for BookStorageRepositoryImplTest decrementQuantityByBookId method")
    @Test
    void givenBookId_whenDecrementQuantityByBookIdInvoked_thenBookQuantityDecremented() {
        // arrange
        Long bookId = 1L;

        // act
        bookStorageRepository.decrementQuantityByBookId(bookId);

        // assert
        assertEquals(2, bookStorageRepository.getQuantityByBookId(bookId));
    }

    @DisplayName("Integration negative test for BookStorageRepositoryImplTest decrementQuantityByBookId method")
    @Test
    void givenBookIdWithZeroQuantityInBookStorage_whenDecrementQuantityByBookIdInvoked_thenBookStorageIllegalReduceQuantityExceptionThrown() {
        // arrange
        Long bookIdWithZeroQuantity = 4L;

        // act and assert
        assertThrows(BookStorageIllegalReduceQuantityException.class, () -> bookStorageRepository.decrementQuantityByBookId(bookIdWithZeroQuantity));
    }

    @DisplayName("Integration positive test for BookStorageRepositoryImplTest getQuantityByBookId method")
    @Test
    void givenBookId_whenGetQuantityByBookIdInvoked_thenBookQuantityReturned() {
        // arrange
        Long bookId = 1L;

        // act
        bookStorageRepository.getQuantityByBookId(bookId);

        // assert
        assertEquals(3, bookStorageRepository.getQuantityByBookId(bookId));
    }

    @DisplayName("Integration negative test for BookStorageRepositoryImplTest getQuantityByBookId method")
    @Test
    void givenNonexistentBookId_whenGetQuantityByBookIdInvoked_thenBookStorageNotFoundExceptionThrown() {
        // arrange
        Long nonexistentBookId = 9999L;

        // act and assert
        assertThrows(BookStorageNotFoundException.class, () -> bookStorageRepository.getQuantityByBookId(nonexistentBookId));
    }

    @DisplayName("Integration positive test for BookStorageRepositoryImplTest getBookStorageByBookId method")
    @Test
    void givenBookId_whenGetBookStorageByBookIdInvoked_thenBookStorageReturned() {
        // arrange
        Long bookId = 1L;

        // act
        BookStorage bookStorage = bookStorageRepository.getBookStorageByBookId(bookId);

        // assert
        assertEquals(bookId, bookStorage.getBook().getId());
        assertEquals(3,bookStorage.getQuantity());
    }

    @DisplayName("Integration negative test for BookStorageRepositoryImplTest getBookStorageByBookId method")
    @Test
    void givenNonexistentBookId_whenGetBookStorageByBookIdInvoked_thenBookStorageNotFoundExceptionThrown() {
        // arrange
        Long nonexistentBookId = 9999L;

        // act and assert
        assertThrows(BookStorageNotFoundException.class, () -> bookStorageRepository.getBookStorageByBookId(nonexistentBookId));
    }

    @DisplayName("Integration positive test for BookStorageRepositoryImplTest deleteBookStorageByBookId method")
    @Test
    void givenBookId_whenDeleteBookStorageByBookIdInvoked_thenBookStorageDeleted() {
        // arrange
        Long bookId = 1L;

        // act
        bookStorageRepository.deleteBookStorageByBookId(bookId);

        // assert
        assertThrows(BookStorageNotFoundException.class, () -> bookStorageRepository.getBookStorageByBookId(bookId));
    }

    @DisplayName("Integration negative test for BookStorageRepositoryImplTest deleteBookStorageByBookId method")
    @Test
    void givenNonexistentBookId_whenDeleteBookStorageByBookIdInvoked_thenNothing() {
        // arrange
        Long nonexistentBookId = 9999L;

        // act and assert
        bookStorageRepository.deleteBookStorageByBookId(nonexistentBookId);
    }
}