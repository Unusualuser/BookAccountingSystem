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
import ru.example.model.BookStorage;
import ru.example.model.Request;
import ru.example.model.User;
import ru.example.model.fieldenum.RequestStatus;
import ru.example.model.fieldenum.UserRole;
import ru.example.repository.BookStorageRepository;
import ru.example.service.BookService;
import ru.example.service.RequestService;
import ru.example.util.EmailSender;

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
    private final Long invalidBookIdForTest = -1L;

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
    void givenBookIdAndAdditionalQuantity_whenAddQuantityByBookIdCloseRequestsIfExistsAndNotifyUsersInvoked_thenBookServiceGetBookByIdRequestServiceCloseBatchRequestsByBookIdAndBatchRepoAddQuantityMethodsCalledAndUsersNotified() {
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
        when(bookService.getBookById(bookId)).thenReturn(bookStorage.getBook());

        // act
        bookStorageService.addQuantityByBookIdCloseRequestsIfExistsAndNotifyUsers(bookId, additionalQuantity);

        // assert
        verify(bookService, times(1)).getBookById(bookId);
        verify(requestService, times(1)).closeBatchRequestsByBookIdAndBatch(bookId, additionalQuantity);
        verify(bookStorageRepository, times(1)).addQuantityByBookId(bookId, additionalQuantity);
        verify(emailSender, times(1)).sendMessage(eq(request.getUser().getEmail()), anyString(), anyString());
    }

    @DisplayName("JUnit negative test for BookStorageServiceImplTest addQuantityByBookIdCloseRequestsIfExistsAndNotifyUsers method")
    @Test
    void givenNonexistentBookIdAndAdditionalQuantity_whenAddQuantityByBookIdCloseRequestsIfExistsAndNotifyUsersInvoked_thenBookNotFoundExceptionThrown() {
        // arrange
        Long nonexistentBookId = invalidBookIdForTest;
        Long additionalQuantity = 2L;

        doThrow(BookNotFoundException.class).when(bookService).getBookById(nonexistentBookId);

        // act and assert
        assertThrows(BookNotFoundException.class, () -> bookStorageService.addQuantityByBookIdCloseRequestsIfExistsAndNotifyUsers(nonexistentBookId, additionalQuantity));
    }

    @DisplayName("JUnit positive test for BookStorageServiceImplTest decrementQuantityByBookId method")
    @Test
    void givenBookId_whenDecrementQuantityByBookIdInvoked_thenRepoDecrementQuantityByBookIdCalled() {
        // act
        Long bookId = bookStorage.getBook().getId();

        // arrange
        bookStorageService.decrementQuantityByBookId(bookId);

        // assert
        verify(bookStorageRepository, times(1)).decrementQuantityByBookId(bookId);
    }

    @DisplayName("JUnit positive test for BookStorageServiceImplTest deleteBookStorageByBookId method")
    @Test
    void givenBookId_whenDeleteBookStorageByBookIdInvoked_thenRepoDeleteBookStorageByBookIdCalled() {
        // act
        Long bookId = bookStorage.getBook().getId();

        // arrange
        bookStorageService.deleteBookStorageByBookId(bookId);

        // assert
        verify(bookStorageRepository, times(1)).deleteBookStorageByBookId(bookId);
    }

    @DisplayName("JUnit positive test for BookStorageServiceImplTest reduceQuantityByBookId method")
    @Test
    void givenBookIdAndQuantityToReduce_whenReduceQuantityByBookIdInvoked_thenBookServiceGetBookByIdAndRepoReduceQuantityByBookIdCalled() {
        // act
        Long bookId = bookStorage.getBook().getId();
        Long quantityToReduce = 2L;

        // arrange
        bookStorageService.reduceQuantityByBookId(bookId, quantityToReduce);

        // assert
        verify(bookService, times(1)).getBookById(bookId);
        verify(bookStorageRepository, times(1)).reduceQuantityByBookId(bookId, quantityToReduce);
    }

    @DisplayName("JUnit negative test for BookStorageServiceImplTest reduceQuantityByBookId method")
    @Test
    void givenNonexistentBookIdAndQuantityToReduce_whenReduceQuantityByBookIdInvoked_thenBookNotFoundExceptionThrown() {
        // arrange
        Long nonexistentBookId = invalidBookIdForTest;
        Long quantityToReduce = 1L;

        doThrow(BookNotFoundException.class).when(bookService).getBookById(nonexistentBookId);

        // act and assert
        assertThrows(BookNotFoundException.class, () -> bookStorageService.reduceQuantityByBookId(nonexistentBookId, quantityToReduce));
    }

    @DisplayName("JUnit positive test for BookStorageServiceImplTest getQuantityByBookId method")
    @Test
    void givenBookId_whenGetQuantityByBookIdInvoked_thenBookServiceGetBookByIdAndRepoGetQuantityByBookIdCalled() {
        // act
        Long bookId = bookStorage.getBook().getId();

        // arrange
        bookStorageService.getQuantityByBookId(bookId);

        // assert
        verify(bookService, times(1)).getBookById(bookId);
        verify(bookStorageRepository, times(1)).getQuantityByBookId(bookId);
    }

    @DisplayName("JUnit negative test for BookStorageServiceImplTest getQuantityByBookId method")
    @Test
    void givenNonexistentBookId_whenGetQuantityByBookIdInvoked_thenBookStorageServiceOperationExceptionThrown() {
        // arrange
        Long nonexistentBookId = invalidBookIdForTest;

        doThrow(BookNotFoundException.class).when(bookService).getBookById(nonexistentBookId);

        // act and assert
        assertThrows(BookNotFoundException.class, () -> bookStorageService.getQuantityByBookId(nonexistentBookId));
    }
}