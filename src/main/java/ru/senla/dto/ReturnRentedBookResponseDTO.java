package ru.senla.dto;

import java.io.Serializable;

public class ReturnRentedBookResponseDTO implements Serializable {
    private Long changedBookHistoryId;

    public ReturnRentedBookResponseDTO() {
    }

    public ReturnRentedBookResponseDTO(Long changedBookHistoryId) {
        this.changedBookHistoryId = changedBookHistoryId;
    }

    public Long getChangedBookHistoryId() {
        return changedBookHistoryId;
    }

    public void setChangedBookHistoryId(Long changedBookHistoryId) {
        this.changedBookHistoryId = changedBookHistoryId;
    }
}
