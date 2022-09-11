package ru.senla.service.impl;

import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.senla.exception.BookServiceOperationException;
import ru.senla.model.Book;
import ru.senla.repository.BookHistoryRepository;
import ru.senla.repository.BookRepository;
import ru.senla.repository.BookStorageRepository;
import ru.senla.repository.RequestRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private BookHistoryRepository bookHistoryRepository;
    @Mock
    private BookStorageRepository bookStorageRepository;
    @Mock
    private RequestRepository requestRepository;
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

    @DisplayName("JUnit negative test for BookServiceImplTest saveBook method")
    @Test
    void givenNullBook_whenSaveBookInvoked_thenBookServiceOperationExceptionThrown() {
        // arrange
        Book nullBook = null;

        doThrow(NullPointerException.class).when(bookRepository).saveBook(nullBook);

        // act and assert
        assertThrows(BookServiceOperationException.class, () -> bookService.saveBook(nullBook));
    }

    @DisplayName("JUnit positive test for BookServiceImplTest updateBookInfo method")
    @Test
    void givenBookInfo_whenUpdateBookInfoInvoked_thenRepoGetBookByIdBookCalledAndInfoUpdated() {
        // arrange
        Book bookToUpdate = new Book(1L, "название", 2021, "автор", "описание");
        Long bookId = bookToUpdate.getId();
        String newName = "новое название";
        Integer newPublicationYear = 2022;
        String newAuthor = "новый автор";
        String newDescription = "новое описание";

        when(bookRepository.getBookById(bookId)).thenReturn(bookToUpdate);

        // act
        bookService.updateBookInfo(bookId, newName, newPublicationYear, newAuthor, newDescription);

        // assert
        verify(bookRepository, times(1)).getBookById(bookId);
        assertEquals(bookToUpdate.getName(), newName);
        assertEquals(bookToUpdate.getPublicationYear(), newPublicationYear);
        assertEquals(bookToUpdate.getAuthor(), newAuthor);
        assertEquals(bookToUpdate.getDescription(), newDescription);
    }

    @DisplayName("JUnit negative test for BookServiceImplTest updateBookInfo method")
    @Test
    void givenInvalidBookId_whenUpdateBookInfoInvoked_thenBookServiceOperationExceptionThrown() {
        // arrange
        Long invalidBookId = invalidBook.getId();

        doThrow(ObjectNotFoundException.class).when(bookRepository).getBookById(invalidBookId);

        // act and assert
        assertThrows(BookServiceOperationException.class, () -> bookService.updateBookInfo(invalidBookId, null, null, null, null));
    }

    @DisplayName("JUnit positive test for BookServiceImplTest deleteBookById method")
    @Test
    void givenBookId_whenDeleteBookByIdInvoked_thenRepoDeleteMethodsCalled() {
        // arrange
        Long bookId = book.getId();

        // act
        bookService.deleteBookById(bookId);

        // assert
        verify(bookHistoryRepository, times(1)).deleteBookHistoriesByBookId(bookId);
        verify(bookStorageRepository, times(1)).deleteBookStoragesByBookId(bookId);
        verify(requestRepository, times(1)).deleteRequestsByBookId(bookId);
        verify(bookRepository, times(1)).deleteBookById(bookId);
    }

    @DisplayName("JUnit negative test for BookServiceImplTest deleteBookById method")
    @Test
    void givenInvalidBookId_whenDeleteBookByIdInvoked_thenBookServiceOperationExceptionThrown() {
        // arrange
        Long invalidBookId = invalidBook.getId();

        doThrow(ObjectNotFoundException.class).when(bookRepository).deleteBookById(invalidBookId);

        // act and assert
        assertThrows(BookServiceOperationException.class, () -> bookService.deleteBookById(invalidBookId));
    }

    @DisplayName("JUnit positive test for BookServiceImplTest getBookById method")
    @Test
    void givenBookId_whenGetBookByIdInvoked_thenRepoGetBookByIdMethodCalled() {
        // arrange
        Long bookId = book.getId();

        // act
        bookService.getBookById(bookId);

        // assert
        verify(bookRepository, times(1)).getBookById(bookId);
    }

    @DisplayName("JUnit negative test for BookServiceImplTest getBookById method")
    @Test
    void givenInvalidBookId_whenGetBookByIdInvoked_thenBookServiceOperationExceptionThrown() {
        // arrange
        Long invalidBookId = invalidBook.getId();

        doThrow(ObjectNotFoundException.class).when(bookRepository).getBookById(invalidBookId);

        // act and assert
        assertThrows(BookServiceOperationException.class, () -> bookService.getBookById(invalidBookId));
    }

    @DisplayName("JUnit positive test for BookServiceImplTest getAllBooks method")
    @Test
    void whenGetAllBooksInvoked_thenRepoGetBookAllBooksMethodCalled() {
        // act
        bookService.getAllBooks();

        // assert
        verify(bookRepository, times(1)).getAllBooks();
    }
}