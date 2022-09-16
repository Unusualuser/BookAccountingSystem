package ru.senla.dto;

import java.io.Serializable;

public class RentBookResponseDto implements Serializable {
    private Long rentedBookId;

    public RentBookResponseDto() {
    }

    public RentBookResponseDto(Long rentedBookId) {
        this.rentedBookId = rentedBookId;
    }

    public Long getRentedBookId() {
        return rentedBookId;
    }

    public void setRentedBookId(Long rentedBookId) {
        this.rentedBookId = rentedBookId;
    }
}
