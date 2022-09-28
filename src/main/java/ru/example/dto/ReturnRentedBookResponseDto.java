package ru.example.dto;

import java.io.Serializable;

public class ReturnRentedBookResponseDto implements Serializable {
    private Long closedBookHistoryId;

    public ReturnRentedBookResponseDto() {
    }

    public ReturnRentedBookResponseDto(Long changedBookHistoryId) {
        this.closedBookHistoryId = changedBookHistoryId;
    }

    public Long getChangedBookHistoryId() {
        return closedBookHistoryId;
    }

    public void setChangedBookHistoryId(Long changedBookHistoryId) {
        this.closedBookHistoryId = changedBookHistoryId;
    }
}
