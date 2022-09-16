package ru.senla.service.impl;

import org.hibernate.exception.SQLGrammarException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.senla.exception.BookNotFoundException;
import ru.senla.exception.RequestServiceOperationException;
import ru.senla.model.Book;
import ru.senla.model.Request;
import ru.senla.model.User;
import ru.senla.model.fieldenum.UserRole;
import ru.senla.repository.RequestRepository;
import ru.senla.service.BookService;
import ru.senla.service.BookStorageService;
import ru.senla.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
    void givenBookIdAndUserLogin_whenRequestBookByIdAndUserLoginInvoked_thenServicesGetBookGetUserAndRepoSaveRequestMethodsCalled() {
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

        when(bookService.getBookById(bookId)).thenReturn(book);
        when(userService.getUserByLogin(userLogin)).thenReturn(user);

        // arrange
        requestService.requestBookByIdAndUserLogin(bookId, userLogin);

        // assert
        verify(bookService, times(1)).getBookById(bookId);
        verify(userService, times(1)).getUserByLogin(userLogin);
        verify(requestRepository, times(1)).saveRequest(captor.capture());
        assertEquals(captor.getValue().getBook(), book);
        assertEquals(captor.getValue().getUser(), user);
    }

//    @DisplayName("JUnit negative test for RequestServiceImplTest requestBookByIdAndUserLogin method")
//    @Test
//    void givenInvalidBookIdAndUserLogin_whenRequestBookByIdAndUserLoginInvoked_thenBookStorageServiceOperationExceptionThrown() {
//        // arrange
//        Long invalidBookId = invalidBookIdForTest;
//        String userLogin = "login";
//
//        doThrow(NullPointerException.class).when(bookService).getBookById(invalidBookId);
//
//        // act and assert
//        assertThrows(RequestServiceOperationException.class, () -> requestService.requestBookByIdAndUserLogin(invalidBookId, userLogin));
//    }

    @DisplayName("JUnit positive test for RequestServiceImplTest getRequestsByBookIdForPeriod method")
    @Test
    void givenBookIdBeginDttmAndEndDttm_whenGetRequestsByBookIdForPeriodInvoked_thenServiceBookContainsCheckAndRepoGetRequestsByBookIdForPeriodCalled() {
        // act
        Long bookId = bookIdForTest;
        LocalDateTime beginDttm = LocalDateTime.of(2022, 8, 20, 10, 10, 10);
        LocalDateTime endDttm = LocalDateTime.now();

        // arrange
        requestService.getRequestsByBookIdForPeriod(bookId, beginDttm, endDttm);

        // assert
        verify(bookService, times(1)).throwBookNotFoundExceptionIfBookByIdNotContains(eq(bookId), anyString());
        verify(requestRepository, times(1)).getRequestsByBookIdForPeriod(bookId, beginDttm, endDttm);
    }

//    @DisplayName("JUnit negative test for RequestServiceImplTest getRequestsByBookIdForPeriod method")
//    @Test
//    void givenNullBookIdBeginDttmAndEndDttm_whenGetRequestsByBookIdForPeriodInvoked_thenBookStorageServiceOperationExceptionThrown() {
//        // arrange
//        Long invalidBookId = null;
//        LocalDateTime beginDttm = LocalDateTime.of(2022, 8, 20, 10, 10, 10);
//        LocalDateTime endDttm = LocalDateTime.now();
//
//        doThrow(SQLGrammarException.class).when(requestRepository).getRequestsByBookIdForPeriod(invalidBookId, beginDttm, endDttm);
//
//        // act and assert
//        assertThrows(RequestServiceOperationException.class, () -> requestService.getRequestsByBookIdForPeriod(invalidBookId, beginDttm, endDttm));
//    }

    @DisplayName("JUnit positive test for RequestServiceImplTest getAllRequestsByBookId method")
    @Test
    void givenBookId_whenGetAllRequestsByBookIdInvoked_thenServiceBookContainsCheckAndRepoGetRequestsByBookIdForPeriodCalled() {
        // act
        Long bookId = bookIdForTest;

        // arrange
        requestService.getAllRequestsByBookId(bookId);

        // assert
        verify(bookService, times(1)).throwBookNotFoundExceptionIfBookByIdNotContains(eq(bookId), anyString());
        verify(requestRepository, times(1)).getAllRequestsByBookId(bookId);
    }

//    @DisplayName("JUnit negative test for RequestServiceImplTest getAllRequestsByBookId method")
//    @Test
//    void givenNonexistentBookId_whenGetAllRequestsByBookIdInvoked_thenBookNotFoundExceptionThrown() {
//        // arrange
//        Long nonexistentBookId = invalidBookIdForTest;
//
//        doThrow(BookNotFoundException.class).when(bookService).throwBookNotFoundExceptionIfBookByIdNotContains(eq(nonexistentBookId), anyString());
//
//        // act and assert
//        assertThrows(BookNotFoundException.class, () -> requestService.getAllRequestsByBookId(nonexistentBookId));
//    }

    @DisplayName("JUnit positive test for RequestServiceImplTest closeBatchRequestsByBookIdAndBatch method")
    @Test
    void givenBookIdAndBatch_whenCloseBatchRequestsByBookIdAndBatchInvoked_thenServiceBookContainsCheckAndRepoCloseBatchRequestsByBookIdAndBatchCalled() {
        // act
        Long bookId = bookIdForTest;
        Long batch = 5L;

        // arrange
        requestService.closeBatchRequestsByBookIdAndBatch(bookId, batch);

        // assert
        verify(bookService, times(1)).throwBookNotFoundExceptionIfBookByIdNotContains(eq(bookId), anyString());
        verify(requestRepository, times(1)).closeBatchRequestsByBookIdAndBatch(bookId, batch);
    }

//    @DisplayName("JUnit negative test for RequestServiceImplTest closeBatchRequestsByBookIdAndBatch method")
//    @Test
//    void givenNonexistentBookIdAndBatch_whenCloseBatchRequestsByBookIdAndBatchInvoked_thenBookNotFoundExceptionThrown() {
//        // arrange
//        Long nonexistentBookId = invalidBookIdForTest;
//        Long batch = 5L;
//
//        doThrow(BookNotFoundException.class).when(bookService).throwBookNotFoundExceptionIfBookByIdNotContains(eq(nonexistentBookId), anyString());
//
//        // act and assert
//        assertThrows(BookNotFoundException.class, () -> requestService.closeBatchRequestsByBookIdAndBatch(nonexistentBookId, batch));
//    }

    @DisplayName("JUnit positive test for RequestServiceImplTest deleteRequestsByBookId method")
    @Test
    void givenBookId_whenDeleteRequestsByBookIdInvoked_thenServiceBookContainsCheckAndRepoDeleteRequestsByBookIdCalled() {
        // act
        Long bookId = bookIdForTest;

        // arrange
        requestService.deleteRequestsByBookId(bookId);

        // assert
        verify(bookService, times(1)).throwBookNotFoundExceptionIfBookByIdNotContains(eq(bookId), anyString());
        verify(requestRepository, times(1)).deleteRequestsByBookId(bookId);
    }

//    @DisplayName("JUnit negative test for RequestServiceImplTest deleteRequestsByBookId method")
//    @Test
//    void givenNonexistentBookId_whenDeleteRequestsByBookIdInvoked_thenBookNotFoundExceptionThrown() {
//        // arrange
//        Long nonexistentBookId = invalidBookIdForTest;
//
//        doThrow(BookNotFoundException.class).when(bookService).throwBookNotFoundExceptionIfBookByIdNotContains(eq(nonexistentBookId), anyString());
//
//        // act and assert
//        assertThrows(BookNotFoundException.class, () -> requestService.deleteRequestsByBookId(nonexistentBookId));
//    }
}