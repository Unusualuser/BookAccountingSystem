package ru.senla.service.impl;

import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.senla.exception.BookHistoryServiceOperationException;
import ru.senla.model.Book;
import ru.senla.model.BookHistory;
import ru.senla.model.User;
import ru.senla.model.fieldenum.UserRole;
import ru.senla.repository.BookHistoryRepository;
import ru.senla.repository.BookRepository;
import ru.senla.repository.BookStorageRepository;
import ru.senla.repository.UserRepository;

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
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookStorageRepository bookStorageRepository;
    @InjectMocks
    private BookHistoryServiceImpl bookHistoryService;
    private BookHistory bookHistory;
    private BookHistory invalidBookHistory;

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
    void givenBookIdAndUserLogin_whenRentBookInvoked_thenReposMethodsCalledReturnDeadlineSetAndNewBookHistorySaved() {
        // arrange
        Book book = bookHistory.getBook();
        User user = bookHistory.getUser();
        Long bookId = book.getId();
        String userLogin = user.getLogin();
        int bookRentDurationInMonth = 2;
        bookHistoryService.setBookRentDurationInMonth(bookRentDurationInMonth);

        ArgumentCaptor<BookHistory> captor = ArgumentCaptor.forClass(BookHistory.class);

        when(bookRepository.getBookById(bookId)).thenReturn(book);
        when(userRepository.getUserByLogin(userLogin)).thenReturn(user);

        // act
        bookHistoryService.rentBook(bookId, userLogin);

        // assert
        verify(bookStorageRepository, times(1)).decrementQuantityByBookId(bookId);
        verify(bookRepository, times(1)).getBookById(bookId);
        verify(userRepository, times(1)).getUserByLogin(userLogin);
        verify(bookHistoryRepository, times(1)).saveBookHistory(captor.capture());
        assertEquals(captor.getValue().getReturnDeadlineDate(), LocalDate.now().plusMonths(bookRentDurationInMonth));
    }

    @DisplayName("JUnit negative test for BookHistoryServiceImplTest rentBook method")
    @Test
    void givenInvalidBookId_whenRentBookInvoked_thenBookHistoryServiceOperationExceptionThrown() {
        // arrange
        Long invalidBookId = -1L;
        String userLogin = "login";

        doThrow(ObjectNotFoundException.class).when(bookRepository).getBookById(invalidBookId);

        // act and assert
        assertThrows(BookHistoryServiceOperationException.class, () -> bookHistoryService.rentBook(invalidBookId, userLogin));
    }

    @DisplayName("JUnit positive test for BookHistoryServiceImplTest returnRentedBook method")
    @Test
    void givenBookHistoryId_whenReturnRentedBookInvoked_thenGetBookHistoryByIdAndIncrementQuantityByBookIdCalledReturnDateSet() {
        // arrange
        Long bookHistoryId = bookHistory.getId();
        Long bookId = bookHistory.getBook().getId();

        when(bookHistoryRepository.getBookHistoryById(bookHistoryId)).thenReturn(bookHistory);

        // act
        bookHistoryService.returnRentedBook(bookHistoryId);

        // assert
        verify(bookHistoryRepository, times(1)).getBookHistoryById(bookHistoryId);
        verify(bookStorageRepository, times(1)).incrementQuantityByBookId(bookId);
        assertEquals(bookHistory.getReturnDate(), LocalDate.now());
    }

    @DisplayName("JUnit negative test for BookHistoryServiceImplTest returnRentedBook method")
    @Test
    void givenInvalidBookHistoryId_whenReturnRentedBookInvoked_thenBookHistoryServiceOperationExceptionThrown() {
        // arrange
        Long invalidBookHistoryId = invalidBookHistory.getId();

        doThrow(ObjectNotFoundException.class).when(bookHistoryRepository).getBookHistoryById(invalidBookHistoryId);

        // act and assert
        assertThrows(BookHistoryServiceOperationException.class, () -> bookHistoryService.returnRentedBook(invalidBookHistoryId));
    }

    @DisplayName("JUnit positive test for BookHistoryServiceImplTest getFullBookHistoryByBookId method")
    @Test
    void givenBookId_whenGetFullBookHistoryByBookIdInvoked_thenRepoGetFullBookHistoryByBookIdCalled() {
        // arrange
        Long bookId = bookHistory.getBook().getId();

        // act
        bookHistoryService.getFullBookHistoryByBookId(bookId);

        // assert
        verify(bookHistoryRepository, times(1)).getFullBookHistoryByBookId(bookId);
    }

    @DisplayName("JUnit negative test for BookHistoryServiceImplTest getFullBookHistoryByBookId method")
    @Test
    void givenInvalidBookId_whenGetFullBookHistoryByBookIdInvoked_thenBookHistoryServiceOperationExceptionThrown() {
        // arrange
        Long invalidBookId = -1L;

        doThrow(ObjectNotFoundException.class).when(bookHistoryRepository).getFullBookHistoryByBookId(invalidBookId);

        // act and assert
        assertThrows(BookHistoryServiceOperationException.class, () -> bookHistoryService.getFullBookHistoryByBookId(invalidBookId));
    }

    @DisplayName("JUnit positive test for BookHistoryServiceImplTest getBookHistoriesByBookIdForPeriod method")
    @Test
    void givenBookIdBeginDateAndEndDate_whenGetBookHistoriesByBookIdForPeriodInvoked_thenRepoGetBookHistoriesByBookIdForPeriodCalled() {
        // arrange
        Long bookId = bookHistory.getBook().getId();
        LocalDate beginDate = LocalDate.of(2022, 7, 13);
        LocalDate endDate = LocalDate.now();

        // act
        bookHistoryService.getBookHistoriesByBookIdForPeriod(bookId, beginDate, endDate);

        // assert
        verify(bookHistoryRepository, times(1)).getBookHistoriesByBookIdForPeriod(bookId, beginDate, endDate);
    }

    @DisplayName("JUnit negative test for BookHistoryServiceImplTest getBookHistoriesByBookIdForPeriod method")
    @Test
    void givenInvalidBookIdBeginDateAndEndDate_whenGetBookHistoriesByBookIdForPeriodInvoked_thenBookHistoryServiceOperationExceptionThrown() {
        // arrange
        Long invalidBookId = -1L;
        LocalDate beginDate = LocalDate.of(2022, 7, 13);
        LocalDate endDate = LocalDate.now();

        doThrow(ObjectNotFoundException.class).when(bookHistoryRepository).getBookHistoriesByBookIdForPeriod(invalidBookId, beginDate, endDate);

        // act and assert
        assertThrows(BookHistoryServiceOperationException.class, () -> bookHistoryService.getBookHistoriesByBookIdForPeriod(invalidBookId, beginDate, endDate));
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
    void givenInvalidBookHistoryId_whenGetBookHistoryByIdInvoked_thenBookHistoryServiceOperationExceptionThrown() {
        // arrange
        Long invalidBookHistoryId = invalidBookHistory.getId();

        doThrow(ObjectNotFoundException.class).when(bookHistoryRepository).getBookHistoryById(invalidBookHistoryId);

        // act and assert
        assertThrows(BookHistoryServiceOperationException.class, () -> bookHistoryService.getBookHistoryById(invalidBookHistoryId));
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