package ru.example.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.example.exception.BookNotFoundException;
import ru.example.model.Book;
import ru.example.repository.BookRepository;


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
class BookRepositoryImplTest {
    @Autowired
    BookRepository bookRepository;

    @DisplayName("Integration positive test for BookRepositoryImplTest saveBook method")
    @Test
    void givenBook_whenSaveBookInvoked_thenBookSaved() {
        // arrange
        Book book = new Book("bookForTest", 2022, "author", "description");

        // act
        bookRepository.saveBook(book);

        // assert
        Book savedBook = bookRepository.getBookById(5L);
        assertEquals(book.getId(), savedBook.getId());
        assertEquals(book.getName(), savedBook.getName());
        assertEquals(book.getPublicationYear(), savedBook.getPublicationYear());
        assertEquals(book.getAuthor(), savedBook.getAuthor());
        assertEquals(book.getDescription(), savedBook.getDescription());
    }

    @DisplayName("Integration positive test for BookRepositoryImplTest deleteBook method")
    @SqlGroup({
            @Sql(value = "classpath:dbscripts/truncateTables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:dbscripts/insertAndUpdate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:dbscripts/insertNewBookAndDeleteRowForItInBookStorage.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    @Test
    void givenBook_whenDeleteBookInvoked_thenBookHistoriesDeleted() {
        // arrange
        Book bookToDelete = new Book(5L, null, null, null, null);

        // act
        bookRepository.deleteBook(bookToDelete);

        // assert
        assertEquals(0, bookRepository.getAllBooks().stream().filter((book -> book.getId().equals(5L))).count());
    }

    @DisplayName("Integration negative test for BookRepositoryImplTest deleteBook method")
    @Test
    void givenNonexistentBook_whenDeleteBookInvoked_thenExceptionThrown() {
        // arrange
        Book bookToDelete = new Book(9999L, null, null, null, null);

        // act
        assertThrows(HibernateOptimisticLockingFailureException.class, () -> bookRepository.deleteBook(bookToDelete));
    }

    @DisplayName("Integration positive test for BookRepositoryImplTest getBookById method")
    @Test
    void givenBookId_whenGetBookByIdInvoked_thenBookReturned() {
        // arrange
        Long bookId = 1L;

        // act
        Book book = bookRepository.getBookById(bookId);

        // assert
        assertEquals(bookId, book.getId());
        assertEquals("451 градус по Фаренгейту", book.getName());
        assertEquals(1953, book.getPublicationYear());
        assertEquals("Рэй Дуглас Брэдбери", book.getAuthor());
        assertEquals("Антиутопия", book.getDescription());
    }

    @DisplayName("Integration negative test for BookRepositoryImplTest getBookById method")
    @Test
    void givenNonexistentBookId_whenGetBookByIdInvoked_thenBookNotFoundExceptionThrown() {
        // arrange
        Long nonexistentBookId = 9999L;

        // act and assert
        assertThrows(BookNotFoundException.class, () -> bookRepository.getBookById(nonexistentBookId));
    }

    @DisplayName("Integration positive test for BookRepositoryImplTest getAllBooks method")
    @Test
    void whenGetAllBooksInvoked_thenBookReturned() {
        // act
        List<Book> allBooks = bookRepository.getAllBooks();

        // assert
        assertEquals(4, allBooks.size());
    }
}