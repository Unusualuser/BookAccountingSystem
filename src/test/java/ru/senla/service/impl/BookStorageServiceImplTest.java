package ru.senla.service.impl;

import org.hibernate.exception.SQLGrammarException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.senla.exception.BookStorageServiceOperationException;
import ru.senla.model.Book;
import ru.senla.model.BookStorage;
import ru.senla.model.Request;
import ru.senla.model.User;
import ru.senla.model.fieldenum.RequestStatus;
import ru.senla.model.fieldenum.UserRole;
import ru.senla.repository.BookStorageRepository;
import ru.senla.repository.RequestRepository;
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
    private RequestRepository requestRepository;
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
    void givenBookIdAndAdditionalQuantity_whenAddQuantityByBookIdCloseRequestsIfExistsAndNotifyUsersInvoked_thenReposMethodsCalledAndUsersNotified() {
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

        when(requestRepository.closeBatchRequestsByBookIdAndBatch(bookId, additionalQuantity)).thenReturn(batchRequests);

        // act
        bookStorageService.addQuantityByBookIdCloseRequestsIfExistsAndNotifyUsers(bookId, additionalQuantity);

        // assert
        verify(requestRepository, times(1)).closeBatchRequestsByBookIdAndBatch(bookId, additionalQuantity);
        verify(bookStorageRepository, times(1)).addQuantityByBookId(bookId, additionalQuantity);
        verify(emailSender, times(1)).sendMessage(eq(request.getUser().getEmail()), anyString(), anyString());
    }

    @DisplayName("JUnit negative test for BookStorageServiceImplTest addQuantityByBookIdCloseRequestsIfExistsAndNotifyUsers method")
    @Test
    void givenNullBookIdAndAdditionalQuantity_whenAddQuantityByBookIdCloseRequestsIfExistsAndNotifyUsersInvoked_thenBookStorageServiceOperationExceptionThrown() {
        // arrange
        Long invalidBookId = null;
        Long additionalQuantity = 2L;

        doThrow(SQLGrammarException.class).when(requestRepository).closeBatchRequestsByBookIdAndBatch(invalidBookId, additionalQuantity);

        // act and assert
        assertThrows(BookStorageServiceOperationException.class,
                () -> bookStorageService.addQuantityByBookIdCloseRequestsIfExistsAndNotifyUsers(invalidBookId, additionalQuantity));
    }

    @DisplayName("JUnit positive test for BookStorageServiceImplTest reduceQuantityByBookId method")
    @Test
    void givenBookIdAndQuantityToReduce_whenReduceQuantityByBookIdInvoked_thenRepoReduceQuantityByBookIdCalled() {
        // act
        Long bookId = bookStorage.getBook().getId();
        Long quantityToReduce = 2L;

        // arrange
        bookStorageService.reduceQuantityByBookId(bookId, quantityToReduce);

        // assert
        verify(bookStorageRepository, times(1)).reduceQuantityByBookId(bookId, quantityToReduce);
    }

    @DisplayName("JUnit negative test for BookStorageServiceImplTest reduceQuantityByBookId method")
    @Test
    void givenNullBookIdAndQuantityToReduce_whenReduceQuantityByBookIdInvoked_thenBookStorageServiceOperationExceptionThrown() {
        // arrange
        Long invalidBookId = null;
        Long quantityToReduce = 1L;

        doThrow(SQLGrammarException.class).when(bookStorageRepository).reduceQuantityByBookId(invalidBookId, quantityToReduce);

        // act and assert
        assertThrows(BookStorageServiceOperationException.class,
                () -> bookStorageService.reduceQuantityByBookId(invalidBookId, quantityToReduce));
    }

    @DisplayName("JUnit positive test for BookStorageServiceImplTest getQuantityByBookId method")
    @Test
    void givenBookId_whenGetQuantityByBookIdInvoked_thenRepoGetQuantityByBookIdCalled() {
        // act
        Long bookId = bookStorage.getBook().getId();

        // arrange
        bookStorageService.getQuantityByBookId(bookId);

        // assert
        verify(bookStorageRepository, times(1)).getQuantityByBookId(bookId);
    }

    @DisplayName("JUnit negative test for BookStorageServiceImplTest getQuantityByBookId method")
    @Test
    void givenNullBookId_whenGetQuantityByBookIdInvoked_thenBookStorageServiceOperationExceptionThrown() {
        // arrange
        Long invalidBookId = null;

        doThrow(SQLGrammarException.class).when(bookStorageRepository).getQuantityByBookId(invalidBookId);

        // act and assert
        assertThrows(BookStorageServiceOperationException.class,
                () -> bookStorageService.getQuantityByBookId(invalidBookId));
    }
}