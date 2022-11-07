package ru.example.dto;

import java.io.Serializable;

public class ReturnRentedBookResponseDto implements Serializable {
    private Long closedBookHistoryId;

    public ReturnRentedBookResponseDto() {
    }

    public ReturnRentedBookResponseDto(Long closedBookHistoryId) {
        this.closedBookHistoryId = closedBookHistoryId;
    }

    public Long getClosedBookHistoryId() {
        return closedBookHistoryId;
    }

    public void setClosedBookHistoryId(Long closedBookHistoryId) {
        this.closedBookHistoryId = closedBookHistoryId;
    }
}
