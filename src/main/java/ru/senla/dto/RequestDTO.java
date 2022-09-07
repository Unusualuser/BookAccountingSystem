package ru.senla.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.senla.model.fieldenum.RequestStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RequestDTO implements Serializable {
    private Long id;
    private Long bookId;
    private Long userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDttm;
    private RequestStatus requestStatus;

    public RequestDTO() {
    }

    public RequestDTO(Long id, Long bookId, Long userId, LocalDateTime createDttm, RequestStatus requestStatus) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.createDttm = createDttm;
        this.requestStatus = requestStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreateDttm() {
        return createDttm;
    }

    public void setCreateDttm(LocalDateTime createDttm) {
        this.createDttm = createDttm;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
}
