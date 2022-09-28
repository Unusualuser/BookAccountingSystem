package ru.example.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.example.exception.BookNotFoundException;
import ru.example.model.Book;
import ru.example.model.Request;
import ru.example.model.User;
import ru.example.model.fieldenum.UserRole;
import ru.example.repository.RequestRepository;
import ru.example.service.BookService;
import ru.example.service.BookStorageService;
import ru.example.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private BookService bookService;
    @Mock
    private UserService userService;
    @Mock
    private BookStorageService bookStorageService;
    @InjectMocks
    private RequestServiceImpl requestService;
    private final Long bookIdForTest = 1L;
    private final Long invalidBookIdForTest = -1L;

    @DisplayName("JUnit positive test for RequestServiceImplTest requestBookByIdAndUserLogin method")
    @Test
    void givenBookIdAndUserLogin_whenRequestBookByIdAndUserLoginInvoked_thenServicesGetQuantityGetBookGetUserAndRepoSaveRequestMethodsCalled() {
        // act
        User user = new User("андрей",
                "адрес",
                "+79999999999",
                1L, "andrew",
                "andrew",
                UserRole.ROLE_USER,
                "email@mail.ru");
        Book book = new Book(bookIdForTest, "книга", 2022, "автор", "описание");
        Long bookId = bookIdForTest;
        String userLogin = user.getLogin();
        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);

        when(bookStorageService.getQuantityByBookId(bookId)).thenReturn(0L);
        when(bookService.getBookById(bookId)).thenReturn(book);
        when(userService.getUserByLogin(userLogin)).thenReturn(user);

        // arrange
        requestService.requestBookByIdAndUserLogin(bookId, userLogin);

        // assert
        verify(bookStorageService, times(1)).getQuantityByBookId(bookId);
        verify(bookService, times(1)).getBookById(bookId);
        verify(userService, times(1)).getUserByLogin(userLogin);
        verify(requestRepository, times(1)).saveRequest(captor.capture());
        assertEquals(captor.getValue().getBook(), book);
        assertEquals(captor.getValue().getUser(), user);
    }

    @DisplayName("JUnit negative test for RequestServiceImplTest requestBookByIdAndUserLogin method")
    @Test
    void givenNonexistentBookIdAndUserLogin_whenRequestBookByIdAndUserLoginInvoked_thenBookNotFoundExceptionThrown() {
        // arrange
        Long nonexistentBookId = invalidBookIdForTest;
        String userLogin = "login";

        doThrow(BookNotFoundException.class).when(bookService).getBookById(nonexistentBookId);

        // act and assert
        assertThrows(BookNotFoundException.class, () -> requestService.requestBookByIdAndUserLogin(nonexistentBookId, userLogin));
    }

    @DisplayName("JUnit positive test for RequestServiceImplTest closeBatchRequestsByBookIdAndBatch method")
    @Test
    void givenBookIdAndBatch_whenCloseBatchRequestsByBookIdAndBatchInvoked_thenRepoCloseBatchRequestsByBookIdAndBatchCalled() {
        // act
        Long bookId = bookIdForTest;
        Long batch = 5L;

        // arrange
        requestService.closeBatchRequestsByBookIdAndBatch(bookId, batch);

        // assert
        verify(requestRepository, times(1)).closeBatchRequestsByBookIdAndBatch(bookId, batch);
    }

    @DisplayName("JUnit positive test for RequestServiceImplTest getRequestsByBookIdForPeriod method")
    @Test
    void givenBookIdBeginDttmAndEndDttm_whenGetRequestsByBookIdForPeriodInvoked_thenBookServiceGetBookByIdAndRepoGetRequestsByBookIdForPeriodCalled() {
        // act
        Long bookId = bookIdForTest;
        LocalDateTime beginDttm = LocalDateTime.of(2022, 8, 20, 10, 10, 10);
        LocalDateTime endDttm = LocalDateTime.now();

        // arrange
        requestService.getRequestsByBookIdForPeriod(bookId, beginDttm, endDttm);

        // assert
        verify(bookService, times(1)).getBookById(bookId);
        verify(requestRepository, times(1)).getRequestsByBookIdForPeriod(bookId, beginDttm, endDttm);
    }

    @DisplayName("JUnit negative test for RequestServiceImplTest getRequestsByBookIdForPeriod method")
    @Test
    void givenNonexistentBookIdBeginDttmAndEndDttm_whenGetRequestsByBookIdForPeriodInvoked_thenBookNotFoundExceptionThrown() {
        // arrange
        Long nonexistentBookId = invalidBookIdForTest;
        LocalDateTime beginDttm = LocalDateTime.of(2022, 8, 20, 10, 10, 10);
        LocalDateTime endDttm = LocalDateTime.now();

        doThrow(BookNotFoundException.class).when(bookService).getBookById(nonexistentBookId);

        // act and assert
        assertThrows(BookNotFoundException.class, () -> requestService.getRequestsByBookIdForPeriod(nonexistentBookId, beginDttm, endDttm));
    }

    @DisplayName("JUnit positive test for RequestServiceImplTest getAllRequestsByBookId method")
    @Test
    void givenBookId_whenGetAllRequestsByBookIdInvoked_thenBookServiceGetBookByIdAndRepoGetRequestsByBookIdForPeriodCalled() {
        // act
        Long bookId = bookIdForTest;

        // arrange
        requestService.getAllRequestsByBookId(bookId);

        // assert
        verify(bookService, times(1)).getBookById(bookId);
        verify(requestRepository, times(1)).getAllRequestsByBookId(bookId);
    }

    @DisplayName("JUnit negative test for RequestServiceImplTest getAllRequestsByBookId method")
    @Test
    void givenNonexistentBookId_whenGetAllRequestsByBookIdInvoked_thenBookNotFoundExceptionThrown() {
        // arrange
        Long nonexistentBookId = invalidBookIdForTest;

        doThrow(BookNotFoundException.class).when(bookService).getBookById(nonexistentBookId);

        // act and assert
        assertThrows(BookNotFoundException.class, () -> requestService.getAllRequestsByBookId(nonexistentBookId));
    }

    @DisplayName("JUnit positive test for RequestServiceImplTest deleteRequestsByBookId method")
    @Test
    void givenBookId_whenDeleteRequestsByBookIdInvoked_thenRepoDeleteRequestsByBookIdCalled() {
        // act
        Long bookId = bookIdForTest;

        // arrange
        requestService.deleteRequestsByBookId(bookId);

        // assert
        verify(requestRepository, times(1)).deleteRequestsByBookId(bookId);
    }
}