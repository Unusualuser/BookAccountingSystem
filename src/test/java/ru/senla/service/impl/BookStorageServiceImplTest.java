package ru.senla.service.impl;

import org.hibernate.exception.SQLGrammarException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.senla.exception.BookNotFoundException;
import ru.senla.exception.BookStorageServiceOperationException;
import ru.senla.model.Book;
import ru.senla.model.BookStorage;
import ru.senla.model.Request;
import ru.senla.model.User;
import ru.senla.model.fieldenum.RequestStatus;
import ru.senla.model.fieldenum.UserRole;
import ru.senla.repository.BookStorageRepository;
import ru.senla.service.BookService;
import ru.senla.service.RequestService;
import ru.senla.util.EmailSender;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookStorageServiceImplTest {
    @Mock
    private BookStorageRepository bookStorageRepository;
    @Mock
    private RequestService requestService;
    @Mock
    private BookService bookService;
    @Mock
    private EmailSender emailSender;
    @InjectMocks
    private BookStorageServiceImpl bookStorageService;
    private BookStorage bookStorage;

    @BeforeEach
    public void init() {
        bookStorage = new BookStorage(1L,
                                        new Book(1L,
                                                "книга",
                                                2022,
                                                "автор",
                                                "описание"),
                                        1L);
    }

    @DisplayName("JUnit positive test for BookStorageServiceImplTest addQuantityByBookIdCloseRequestsIfExistsAndNotifyUsers method")
    @Test
    void givenBookIdAndAdditionalQuantity_whenAddQuantityByBookIdCloseRequestsIfExistsAndNotifyUsersInvoked_thenServicesCloseRequestsBookContainsCheckRepoAddQuantityMethodsCalledAndUsersNotified() {
        // arrange
        Long bookId = bookStorage.getBook().getId();
        Long additionalQuantity = 1L;
        Request request = new Request(1L,
                                        bookStorage.getBook(),
                                        new User("андрей",
                                                "адрес",
                                                "+79999999999",
                                                1L, "andrew",
                                                "andrew",
                                                UserRole.ROLE_USER,
                                                "email@mail.ru"),
                                        LocalDateTime.now(),
                                        RequestStatus.NEW);
        List<Request> batchRequests = List.of(request);

        when(requestService.closeBatchRequestsByBookIdAndBatch(bookId, additionalQuantity)).thenReturn(batchRequests);

        // act
        bookStorageService.addQuantityByBookIdCloseRequestsIfExistsAndNotifyUsers(bookId, additionalQuantity);

        // assert
        verify(bookService, times(1)).throwBookNotFoundExceptionIfBookByIdNotContains(eq(bookId), anyString());
        verify(requestService, times(1)).closeBatchRequestsByBookIdAndBatch(bookId, additionalQuantity);
        verify(bookStorageRepository, times(1)).addQuantityByBookId(bookId, additionalQuantity);
        verify(emailSender, times(1)).sendMessage(eq(request.getUser().getEmail()), anyString(), anyString());
    }

//    @DisplayName("JUnit negative test for BookStorageServiceImplTest addQuantityByBookIdCloseRequestsIfExistsAndNotifyUsers method")
//    @Test
//    void givenNullBookIdAndAdditionalQuantity_whenAddQuantityByBookIdCloseRequestsIfExistsAndNotifyUsersInvoked_thenBookStorageServiceOperationExceptionThrown() {
//        // arrange
//        Long invalidBookId = null;
//        Long additionalQuantity = 2L;
//
//        doThrow(SQLGrammarException.class).when(requestService).closeBatchRequestsByBookIdAndBatch(invalidBookId, additionalQuantity);
//
//        // act and assert
//        assertThrows(BookStorageServiceOperationException.class,
//                () -> bookStorageService.addQuantityByBookIdCloseRequestsIfExistsAndNotifyUsers(invalidBookId, additionalQuantity));
//    }

    @DisplayName("JUnit positive test for BookStorageServiceImplTest reduceQuantityByBookId method")
    @Test
    void givenBookIdAndQuantityToReduce_whenReduceQuantityByBookIdInvoked_thenServiceBookContainsCheckAndRepoReduceQuantityByBookIdCalled() {
        // act
        Long bookId = bookStorage.getBook().getId();
        Long quantityToReduce = 2L;

        // arrange
        bookStorageService.reduceQuantityByBookId(bookId, quantityToReduce);

        // assert
        verify(bookService, times(1)).throwBookNotFoundExceptionIfBookByIdNotContains(eq(bookId), anyString());
        verify(bookStorageRepository, times(1)).reduceQuantityByBookId(bookId, quantityToReduce);
    }

//    @DisplayName("JUnit negative test for BookStorageServiceImplTest reduceQuantityByBookId method")
//    @Test
//    void givenNullBookIdAndQuantityToReduce_whenReduceQuantityByBookIdInvoked_thenBookStorageServiceOperationExceptionThrown() {
//        // arrange
//        Long invalidBookId = null;
//        Long quantityToReduce = 1L;
//
//        doThrow(SQLGrammarException.class).when(bookStorageRepository).reduceQuantityByBookId(invalidBookId, quantityToReduce);
//
//        // act and assert
//        assertThrows(BookStorageServiceOperationException.class,
//                () -> bookStorageService.reduceQuantityByBookId(invalidBookId, quantityToReduce));
//    }

    @DisplayName("JUnit positive test for BookStorageServiceImplTest getQuantityByBookId method")
    @Test
    void givenBookId_whenGetQuantityByBookIdInvoked_thenServiceBookContainsCheckAndRepoGetQuantityByBookIdCalled() {
        // act
        Long bookId = bookStorage.getBook().getId();

        // arrange
        bookStorageService.getQuantityByBookId(bookId);

        // assert
        verify(bookService, times(1)).throwBookNotFoundExceptionIfBookByIdNotContains(eq(bookId), anyString());
        verify(bookStorageRepository, times(1)).getQuantityByBookId(bookId);
    }

//    @DisplayName("JUnit negative test for BookStorageServiceImplTest getQuantityByBookId method")
//    @Test
//    void givenNullBookId_whenGetQuantityByBookIdInvoked_thenBookStorageServiceOperationExceptionThrown() {
//        // arrange
//        Long invalidBookId = null;
//
//        doThrow(SQLGrammarException.class).when(bookStorageRepository).getQuantityByBookId(invalidBookId);
//
//        // act and assert
//        assertThrows(BookStorageServiceOperationException.class, () -> bookStorageService.getQuantityByBookId(invalidBookId));
//    }

    @DisplayName("JUnit positive test for BookStorageServiceImplTest incrementQuantityByBookId method")
    @Test
    void givenBookId_whenIncrementQuantityByBookIdInvoked_thenServiceBookContainsCheckAndRepoIncrementQuantityByBookIdCalled() {
        // act
        Long bookId = bookStorage.getBook().getId();

        // arrange
        bookStorageService.incrementQuantityByBookId(bookId);

        // assert
        verify(bookService, times(1)).throwBookNotFoundExceptionIfBookByIdNotContains(eq(bookId), anyString());
        verify(bookStorageRepository, times(1)).incrementQuantityByBookId(bookId);
    }

//    @DisplayName("JUnit negative test for BookStorageServiceImplTest incrementQuantityByBookId method")
//    @Test
//    void givenNonexistentBookId_whenIncrementQuantityByBookIdInvoked_thenBookNotFoundExceptionThrown() {
//        // arrange
//        Long nonexistentBookId = -1L;
//
//        doThrow(BookNotFoundException.class).when(bookService).throwBookNotFoundExceptionIfBookByIdNotContains(eq(nonexistentBookId), anyString());
//
//        // act and assert
//        assertThrows(BookNotFoundException.class, () -> bookStorageService.incrementQuantityByBookId(nonexistentBookId));
//    }

    @DisplayName("JUnit positive test for BookStorageServiceImplTest decrementQuantityByBookId method")
    @Test
    void givenBookId_whenDecrementQuantityByBookIdInvoked_thenServiceBookContainsCheckAndRepoDecrementQuantityByBookIdCalled() {
        // act
        Long bookId = bookStorage.getBook().getId();

        // arrange
        bookStorageService.decrementQuantityByBookId(bookId);

        // assert
        verify(bookService, times(1)).throwBookNotFoundExceptionIfBookByIdNotContains(eq(bookId), anyString());
        verify(bookStorageRepository, times(1)).decrementQuantityByBookId(bookId);
    }

//    @DisplayName("JUnit negative test for BookStorageServiceImplTest decrementQuantityByBookId method")
//    @Test
//    void givenNonexistentBookId_whenDecrementQuantityByBookIdInvoked_thenBookNotFoundExceptionThrown() {
//        // arrange
//        Long nonexistentBookId = -1L;
//
//        doThrow(BookNotFoundException.class).when(bookService).throwBookNotFoundExceptionIfBookByIdNotContains(eq(nonexistentBookId), anyString());
//
//        // act and assert
//        assertThrows(BookNotFoundException.class, () -> bookStorageService.decrementQuantityByBookId(nonexistentBookId));
//    }

    @DisplayName("JUnit positive test for BookStorageServiceImplTest deleteBookStorageByBookId method")
    @Test
    void givenBookId_whenDeleteBookStorageByBookIdInvoked_thenServiceBookContainsCheckAndRepoDeleteBookStorageByBookIdCalled() {
        // act
        Long bookId = bookStorage.getBook().getId();

        // arrange
        bookStorageService.deleteBookStorageByBookId(bookId);

        // assert
        verify(bookService, times(1)).throwBookNotFoundExceptionIfBookByIdNotContains(eq(bookId), anyString());
        verify(bookStorageRepository, times(1)).deleteBookStorageByBookId(bookId);
    }

//    @DisplayName("JUnit negative test for BookStorageServiceImplTest deleteBookStorageByBookId method")
//    @Test
//    void givenNonexistentBookId_whenDeleteBookStorageByBookIdInvoked_thenBookNotFoundExceptionThrown() {
//        // arrange
//        Long nonexistentBookId = -1L;
//
//        doThrow(BookNotFoundException.class).when(bookService).throwBookNotFoundExceptionIfBookByIdNotContains(eq(nonexistentBookId), anyString());
//
//        // act and assert
//        assertThrows(BookNotFoundException.class, () -> bookStorageService.deleteBookStorageByBookId(nonexistentBookId));
//    }
}