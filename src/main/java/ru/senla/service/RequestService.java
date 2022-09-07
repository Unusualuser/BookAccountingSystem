package ru.senla.service;

import ru.senla.model.Request;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestService {
    void requestBookByIdAndUserLogin(Long bookId, String userLogin);

    List<Request> getRequestsByBookIdForPeriod(Long id, LocalDateTime beginDttm, LocalDateTime endDttm);

    List<Request> getAllRequestsByBookId(Long bookId);
}
