package ru.example.service;

import ru.example.model.Request;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestService {
    void requestBookByIdAndUserLogin(Long bookId, String userLogin);

    List<Request> closeBatchRequestsByBookIdAndBatch(Long bookId, Long batch);

    List<Request> getRequestsByBookIdForPeriod(Long id, LocalDateTime beginDttm, LocalDateTime endDttm);

    List<Request> getAllRequestsByBookId(Long bookId);

    void deleteRequestsByBookId(Long bookId);
}
