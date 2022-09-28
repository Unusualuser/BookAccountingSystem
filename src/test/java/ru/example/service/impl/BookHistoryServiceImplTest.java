package ru.example.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.example.exception.BookHistoryNotFoundException;
import ru.example.exception.BookNotFoundException;
import ru.example.model.Book;
import ru.example.model.BookHistory;
import ru.example.model.User;
import ru.example.model.fieldenum.UserRole;
import ru.example.repository.BookHistoryRepository;
import ru.example.service.BookService;
import ru.example.service.BookStorageService;
import ru.example.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookHistoryServiceImplTest {
    @Mock
    private BookHistoryRepository bookHistoryRepository;
    @Mock
    private BookService bookService;
    @Mock
    private UserService userService;
    @Mock
    private BookStorageService bookStorageService;
    @InjectMocks
    private BookHistoryServiceImpl bookHistoryService;
    private BookHistory bookHistory;
    private BookHistory invalidBookHistory;
    private final Long invalidBookIdForTest = -1L;

    @BeforeEach
    public void init() {
        bookHistory = new BookHistory(1L,
                                        new Book(1L,
                                                "книга",
                                                2022,
                                                "автор",
                                                "описание"),
                                        new User("пользователь",
                                                "адрес",
                                                "+79999999999",
                                                1L,
                                                "login",
                                                "password",
                                                UserRole.ROLE_USER,
                                                "email@mail.ru"),
                                        LocalDate.of(2022, 9, 20),
                                        LocalDate.of(2022, 11, 20),
                                        null);
        invalidBookHistory = new BookHistory(-1L, null, null, null, null, null);
    }

    @DisplayName("JUnit positive test for BookHistoryServiceImplTest rentBook method")
    @Test
    void givenBookIdAndUserLogin_whenRentBookInvoked_thenServicesMethodsAndRepoSaveBookHistoryCalledRentalDateAndReturnDeadlineSet() {
        // arrange
        Book book = bookHistory.getBook();
        User user = bookHistory.getUser();
        Long bookId = book.getId();
        String userLogin = user.getLogin();
        int bookRentDurationInMonth = 2;
        bookHistoryService.setBookRentDurationInMonth(bookRentDurationInMonth);

        ArgumentCaptor<BookHistory> captor = ArgumentCaptor.forClass(BookHistory.class);

        when(bookService.getBookById(bookId)).thenReturn(book);
        when(userService.getUserByLogin(userLogin)).thenReturn(user);

        // act
        bookHistoryService.rentBook(bookId, userLogin);

        // assert
        verify(bookService, times(1)).getBookById(bookId);
        verify(userService, times(1)).getUserByLogin(userLogin);
        verify(bookStorageService, times(1)).decrementQuantityByBookId(bookId);
        verify(bookHistoryRepository, times(1)).saveBookHistory(captor.capture());
        assertEquals(captor.getValue().getReturnDeadlineDate(), LocalDate.now().plusMonths(bookRentDurationInMonth));
        Assertions.assertEquals(captor.getValue().getRentalDate(), LocalDate.now());
    }

    @DisplayName("JUnit negative test for BookHistoryServiceImplTest rentBook method")
    @Test
    void givenNonexistentBookId_whenRentBookInvoked_thenBookNotFoundExceptionThrown() {
        // arrange
        Long nonexistentBookId = invalidBookIdForTest;
        String userLogin = "login";

        doThrow(BookNotFoundException.class).when(bookService).getBookById(nonexistentBookId);

        // act and assert
        assertThrows(BookNotFoundException.class, () -> bookHistoryService.rentBook(nonexistentBookId, userLogin));
    }

    @DisplayName("JUnit positive test for BookHistoryServiceImplTest returnRentedBook method")
    @Test
    void givenBookHistoryId_whenReturnRentedBookInvoked_thenRepoGetBookHistoryByIdAndBookStorageServiceAddQuantityByBookIdCloseRequestsIfExistsAndNotifyUsersCalledReturnDateSet() {
        // arrange
        Long bookHistoryId = bookHistory.getId();
        Long bookId = bookHistory.getBook().getId();

        when(bookHistoryService.getBookHistoryById(bookHistoryId)).thenReturn(bookHistory);

        // act
        bookHistoryService.returnRentedBook(bookHistoryId);

        // assert
        verify(bookHistoryRepository, times(1)).getBookHistoryById(bookHistoryId);
        verify(bookStorageService, times(1)).addQuantityByBookIdCloseRequestsIfExistsAndNotifyUsers(bookId, 1L);
        assertEquals(bookHistory.getReturnDate(), LocalDate.now());
    }

    @DisplayName("JUnit negative test for BookHistoryServiceImplTest returnRentedBook method")
    @Test
    void givenNonexistentBookHistoryId_whenReturnRentedBookInvoked_thenBookHistoryNotFoundExceptionThrown() {
        // arrange
        Long nonexistentBookHistoryId = invalidBookHistory.getId();

        doThrow(BookHistoryNotFoundException.class).when(bookHistoryRepository).getBookHistoryById(nonexistentBookHistoryId);

        // act and assert
        assertThrows(BookHistoryNotFoundException.class, () -> bookHistoryService.returnRentedBook(nonexistentBookHistoryId));
    }

    @DisplayName("JUnit positive test for BookHistoryServiceImplTest deleteBookHistoriesByBookId method")
    @Test
    void givenBookId_whenDeleteBookHistoriesByBookIdInvoked_thenRepoDeleteBookHistoriesByBookIdCalled() {
        // arrange
        Long bookId = bookHistory.getBook().getId();

        // act
        bookHistoryService.deleteBookHistoriesByBookId(bookId);

        // assert
        verify(bookHistoryRepository, times(1)).deleteBookHistoriesByBookId(bookId);
    }

    @DisplayName("JUnit positive test for BookHistoryServiceImplTest getFullBookHistoryByBookId method")
    @Test
    void givenBookId_whenGetFullBookHistoryByBookIdInvoked_thenBookServiceGetBookByIdAndRepoGetFullBookHistoryByBookIdCalled() {
        // arrange
        Long bookId = bookHistory.getBook().getId();

        // act
        bookHistoryService.getFullBookHistoryByBookId(bookId);

        // assert
        verify(bookService, times(1)).getBookById(bookId);
        verify(bookHistoryRepository, times(1)).getFullBookHistoryByBookId(bookId);
    }

    @DisplayName("JUnit negative test for BookHistoryServiceImplTest getFullBookHistoryByBookId method")
    @Test
    void givenNonexistentBookId_whenGetFullBookHistoryByBookIdInvoked_thenBookNotFoundExceptionThrown() {
        // arrange
        Long nonexistentBookId = invalidBookIdForTest;

        doThrow(BookNotFoundException.class).when(bookService).getBookById(nonexistentBookId);

        // act and assert
        assertThrows(BookNotFoundException.class, () -> bookHistoryService.getFullBookHistoryByBookId(nonexistentBookId));
    }

    @DisplayName("JUnit positive test for BookHistoryServiceImplTest getBookHistoriesByBookIdForPeriod method")
    @Test
    void givenBookIdBeginDateAndEndDate_whenGetBookHistoriesByBookIdForPeriodInvoked_thenBookServiceGetBookByIdAndRepoGetBookHistoriesByBookIdForPeriodCalled() {
        // arrange
        Long bookId = bookHistory.getBook().getId();
        LocalDate beginDate = LocalDate.of(2022, 7, 13);
        LocalDate endDate = LocalDate.now();

        // act
        bookHistoryService.getBookHistoriesByBookIdForPeriod(bookId, beginDate, endDate);

        // assert
        verify(bookService, times(1)).getBookById(bookId);
        verify(bookHistoryRepository, times(1)).getBookHistoriesByBookIdForPeriod(bookId, beginDate, endDate);
    }

    @DisplayName("JUnit negative test for BookHistoryServiceImplTest getBookHistoriesByBookIdForPeriod method")
    @Test
    void givenNonexistentBookIdBeginDateAndEndDate_whenGetBookHistoriesByBookIdForPeriodInvoked_thenBookNotFoundExceptionThrown() {
        // arrange
        Long nonexistentBookId = invalidBookIdForTest;
        LocalDate beginDate = LocalDate.of(2022, 7, 13);
        LocalDate endDate = LocalDate.now();

        doThrow(BookNotFoundException.class).when(bookService).getBookById(nonexistentBookId);

        // act and assert
        assertThrows(BookNotFoundException.class, () -> bookHistoryService.getBookHistoriesByBookIdForPeriod(nonexistentBookId, beginDate, endDate));
    }

    @DisplayName("JUnit positive test for BookHistoryServiceImplTest getBookHistoryById method")
    @Test
    void givenBookHistoryId_whenGetBookHistoryByIdInvoked_thenRepoGetBookHistoryByIdCalled() {
        // arrange
        Long bookHistoryId = bookHistory.getId();

        // act
        bookHistoryService.getBookHistoryById(bookHistoryId);

        // assert
        verify(bookHistoryRepository, times(1)).getBookHistoryById(bookHistoryId);
    }

    @DisplayName("JUnit negative test for BookHistoryServiceImplTest getBookHistoryById method")
    @Test
    void givenInvalidBookHistoryId_whenGetBookHistoryByIdInvoked_thenBookHistoryNotFoundExceptionThrown() {
        // arrange
        Long invalidBookHistoryId = invalidBookHistory.getId();

        doThrow(BookHistoryNotFoundException.class).when(bookHistoryRepository).getBookHistoryById(invalidBookHistoryId);

        // act and assert
        assertThrows(BookHistoryNotFoundException.class, () -> bookHistoryService.getBookHistoryById(invalidBookHistoryId));
    }

    @DisplayName("JUnit positive test for BookHistoryServiceImplTest findAndGetExpiredRent method")
    @Test
    void whenFindAndGetExpiredRentInvoked_thenRepoFindAndGetExpiredRentCalled() {
        // act
        bookHistoryService.findAndGetExpiredRent();

        // assert
        verify(bookHistoryRepository, times(1)).findAndGetExpiredRent();
    }
}