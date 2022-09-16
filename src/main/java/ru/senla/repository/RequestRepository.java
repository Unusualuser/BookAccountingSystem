package ru.senla.repository;

import ru.senla.model.Request;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestRepository {
    void saveRequest(Request request);

    List<Request> closeBatchRequestsByBookIdAndBatch(Long bookId, Long batch);

    List<Request> getRequestsByBookIdForPeriod(Long id, LocalDateTime beginDttm, LocalDateTime endDttm);

    List<Request> getAllRequestsByBookId(Long bookId);

    void deleteRequestsByBookId(Long bookId);
}
