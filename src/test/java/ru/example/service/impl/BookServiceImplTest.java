package ru.example.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.example.exception.BookNotFoundException;
import ru.example.model.Book;
import ru.example.repository.BookRepository;
import ru.example.service.BookHistoryService;
import ru.example.service.BookStorageService;
import ru.example.service.RequestService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookHistoryService bookHistoryService;
    @Mock
    private BookStorageService bookStorageService;
    @Mock
    private RequestService requestService;
    @InjectMocks
    private BookServiceImpl bookService;
    private Book book;
    private Book invalidBook;

    @BeforeEach
    public void init() {
        book = new Book(1L, "книга", 2022, "автор", "описание");
        invalidBook = new Book(-1L, null, null, null, null);
    }

    @DisplayName("JUnit positive test for BookServiceImplTest saveBook method")
    @Test
    void givenBook_whenSaveBookInvoked_thenRepoSaveMethodCalled() {
        // arrange
        Book bookToSave = book;

        // act
        bookService.saveBook(bookToSave);

        // assert
        verify(bookRepository, times(1)).saveBook(bookToSave);
    }

    @DisplayName("JUnit positive test for BookServiceImplTest updateBookInfo method")
    @Test
    void givenBookInfo_whenUpdateBookInfoInvoked_thenRepoGetBookByIdBookCalledAndInfoUpdated() {
        // arrange
        Book bookToUpdate = new Book(1L, "название", 2021, "автор", "описание");
        Long bookId = bookToUpdate.getId();
        String newName = "новое название";
        Integer newPublicationYear = null;
        String newAuthor = "новый автор";
        String newDescription = "новое описание";

        when(bookRepository.getBookById(bookId)).thenReturn(bookToUpdate);

        // act
        bookService.updateBookInfo(bookId, newName, newPublicationYear, newAuthor, newDescription);

        // assert
        verify(bookRepository, times(1)).getBookById(bookId);
        assertEquals(bookToUpdate.getName(), newName);
        assertNotEquals(bookToUpdate.getPublicationYear(), newPublicationYear);
        assertEquals(bookToUpdate.getAuthor(), newAuthor);
        assertEquals(bookToUpdate.getDescription(), newDescription);
    }

    @DisplayName("JUnit negative test for BookServiceImplTest updateBookInfo method")
    @Test
    void givenNonexistentBookId_whenUpdateBookInfoInvoked_thenBookNotFoundExceptionThrown() {
        // arrange
        Long nonexistentBookId = invalidBook.getId();

        doThrow(BookNotFoundException.class).when(bookRepository).getBookById(nonexistentBookId);

        // act and assert
        assertThrows(BookNotFoundException.class, () -> bookService.updateBookInfo(nonexistentBookId, null, null, null, null));
    }

    @DisplayName("JUnit positive test for BookServiceImplTest deleteBookById method")
    @Test
    void givenBookId_whenDeleteBookByIdInvoked_thenRepoGetBookByIdServicesAndRepoDeleteMethodsCalled() {
        // arrange
        Book bookToDelete = book;
        Long bookId = bookToDelete.getId();

        when(bookRepository.getBookById(bookId)).thenReturn(bookToDelete);

        // act
        bookService.deleteBookById(bookId);

        // assert
        verify(bookRepository, times(1)).getBookById(bookId);
        verify(bookHistoryService, times(1)).deleteBookHistoriesByBookId(bookId);
        verify(bookStorageService, times(1)).deleteBookStorageByBookId(bookId);
        verify(requestService, times(1)).deleteRequestsByBookId(bookId);
        verify(bookRepository, times(1)).deleteBook(bookToDelete);
    }

    @DisplayName("JUnit negative test for BookServiceImplTest deleteBookById method")
    @Test
    void givenNonexistentBookId_whenDeleteBookByIdInvoked_thenBookNotFoundExceptionThrown() {
        // arrange
        Long nonexistentBookId = invalidBook.getId();

        doThrow(BookNotFoundException.class).when(bookRepository).getBookById(nonexistentBookId);

        // act and assert
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBookById(nonexistentBookId));
    }

    @DisplayName("JUnit positive test for BookServiceImplTest getBookById method")
    @Test
    void givenBookId_whenGetBookByIdInvoked_thenRepoGetBookByIdMethodCalled() {
        // arrange
        Long bookId = book.getId();

        when(bookRepository.getBookById(bookId)).thenReturn(book);

        // act
        Book obtainedBook = bookService.getBookById(bookId);

        // assert
        verify(bookRepository, times(1)).getBookById(bookId);
        assertEquals(obtainedBook.getId(), book.getId());
        assertEquals(obtainedBook.getName(), book.getName());
        assertEquals(obtainedBook.getAuthor(), book.getAuthor());
        assertEquals(obtainedBook.getPublicationYear(), book.getPublicationYear());
        assertEquals(obtainedBook.getDescription(), book.getDescription());
    }

    @DisplayName("JUnit negative test for BookServiceImplTest getBookById method")
    @Test
    void givenNonexistentBookId_whenGetBookByIdInvoked_thenBookNotFoundExceptionThrown() {
        // arrange
        Long nonexistentBookId = invalidBook.getId();

        doThrow(BookNotFoundException.class).when(bookRepository).getBookById(nonexistentBookId);

        // act and assert
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(nonexistentBookId));
    }

    @DisplayName("JUnit positive test for BookServiceImplTest getAllBooks method")
    @Test
    void whenGetAllBooksInvoked_thenRepoGetAllBooksMethodCalled() {
        // act
        bookService.getAllBooks();

        // assert
        verify(bookRepository, times(1)).getAllBooks();
    }
}