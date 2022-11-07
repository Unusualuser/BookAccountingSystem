package ru.example.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.example.model.Book;
import ru.example.model.Request;
import ru.example.model.User;
import ru.example.model.fieldenum.RequestStatus;
import ru.example.repository.RequestRepository;

import java.time.LocalDateTime;
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
class RequestRepositoryImplTest {
    @Autowired
    RequestRepository requestRepository;

    @DisplayName("Integration positive test for RequestRepositoryImplTest saveRequest method")
    @Test
    void givenRequest_whenSaveRequestInvoked_thenRequestSaved() {
        // arrange
        Book book = new Book(4L, null, null, null, null);
        User user = new User(null, null, null, 2L, null, null, null, null);
        Request requestToSave = new Request(book, user);

        // act
        requestRepository.saveRequest(requestToSave);

        // assert
        List<Request> requestList = requestRepository.getRequestsByBookIdForPeriod(4L, LocalDateTime.now().minusMinutes(1), LocalDateTime.now());
        assertEquals(2, requestList.size());
        assertEquals(2, requestList.get(1).getUser().getId());
    }

    @DisplayName("Integration negative test for RequestRepositoryImplTest saveRequest method")
    @Test
    void givenRequestWithNonexistentBook_whenSaveRequestInvoked_thenDataIntegrityViolationExceptionThrown() {
        // arrange
        Book book = new Book(9999L, null, null, null, null);
        User user = new User(null, null, null, 2L, null, null, null, null);
        Request requestToSave = new Request(book, user);

        // act and assert
        assertThrows(DataIntegrityViolationException.class, () -> requestRepository.saveRequest(requestToSave));
    }

    @DisplayName("Integration positive test for RequestRepositoryImplTest getRequestsByBookIdForPeriod method")
    @Test
    void givenBookIdAndPeriod_whenGetRequestsByBookIdForPeriodInvoked_thenRequestsByBookIdForPeriodReturned() {
        // arrange
        Long bookId = 4L;
        LocalDateTime beginDateTime = LocalDateTime.now().minusMinutes(1);
        LocalDateTime endDateTime = LocalDateTime.now();

        // act
        List<Request> requestsForPeriod = requestRepository.getRequestsByBookIdForPeriod(bookId, beginDateTime, endDateTime);

        // assert
        assertEquals(1, requestsForPeriod.size());
    }

    @DisplayName("Integration negative test for RequestRepositoryImplTest getRequestsByBookIdForPeriod method")
    @Test
    void givenNonexistentBookIdAndPeriod_whenGetRequestsByBookIdForPeriodInvoked_thenEmptyListWithBookHistoriesReturned() {
        // arrange
        Long nonexistentBookId = 9999L;
        LocalDateTime beginDateTime = LocalDateTime.now().minusMinutes(1);
        LocalDateTime endDateTime = LocalDateTime.now();

        // act
        List<Request> requestsForPeriod = requestRepository.getRequestsByBookIdForPeriod(nonexistentBookId, beginDateTime, endDateTime);

        // assert
        assertEquals(0, requestsForPeriod.size());
    }

    @DisplayName("Integration positive test for RequestRepositoryImplTest closeBatchRequestsByBookIdAndBatch method")
    @SqlGroup({
            @Sql(value = "classpath:dbscripts/truncateTables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:dbscripts/insertAndUpdate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:dbscripts/insertNewRequests.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    @Test
    void givenBookIdAndBatch_whenCloseBatchRequestsByBookIdAndBatchInvoked_thenBatchRequestsClosed() {
        // arrange
        Long bookId = 4L;
        Long batch = 2L;

        // act
        requestRepository.closeBatchRequestsByBookIdAndBatch(bookId, batch);

        // assert
        List<Request> requests = requestRepository.getAllRequestsByBookId(bookId);
        assertEquals(3, requests.size());
        assertEquals(2, requests.stream().filter(request -> request.getRequestStatus() == RequestStatus.DONE).count());
    }

    @DisplayName("Integration negative test for RequestRepositoryImplTest closeBatchRequestsByBookIdAndBatch method")
    @Test
    void givenNonexistentBookIdAndBatch_whenCloseBatchRequestsByBookIdAndBatchInvoked_thenNothing() {
        // arrange
        Long nonexistentBookId = 9999L;
        Long batch = 2L;

        // act and assert
        requestRepository.closeBatchRequestsByBookIdAndBatch(nonexistentBookId, batch);
    }

    @DisplayName("Integration positive test for RequestRepositoryImplTest getAllRequestsByBookId method")
    @Test
    void givenBookId_whenGetAllRequestsByBookIdInvoked_thenAllRequestsReturned() {
        // arrange
        Long bookId = 4L;

        // act
        List<Request> allRequests = requestRepository.getAllRequestsByBookId(bookId);

        // assert
        assertEquals(1, allRequests.size());
    }

    @DisplayName("Integration negative test for RequestRepositoryImplTest getAllRequestsByBookId method")
    @Test
    void givenNonexistentBookId_whenGetAllRequestsByBookIdInvoked_thenEmptyListReturned() {
        // arrange
        Long nonexistentBookId = 9999L;

        // act and assert
        assertEquals(0, requestRepository.getAllRequestsByBookId(nonexistentBookId).size());
    }

    @DisplayName("Integration positive test for RequestRepositoryImplTest deleteRequestsByBookId method")
    @Test
    void givenBookId_whenDeleteRequestsByBookIdInvoked_thenRequestsDeleted() {
        // arrange
        Long bookId = 4L;

        // act
        requestRepository.deleteRequestsByBookId(bookId);

        // assert
        assertEquals(0, requestRepository.getAllRequestsByBookId(bookId).size());
    }

    @DisplayName("Integration negative test for RequestRepositoryImplTest deleteRequestsByBookId method")
    @Test
    void givenNonexistentBookId_whenDeleteRequestsByBookIdInvoked_thenNothing() {
        // arrange
        Long nonexistentBookId = 9999L;

        // act and assert
        requestRepository.deleteRequestsByBookId(nonexistentBookId);
    }
}