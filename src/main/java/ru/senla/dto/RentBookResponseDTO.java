package ru.senla.dto;

import java.io.Serializable;

public class RentBookResponseDTO implements Serializable {
    private Long rentedBookId;

    public RentBookResponseDTO() {
    }

    public RentBookResponseDTO(Long rentedBookId) {
        this.rentedBookId = rentedBookId;
    }

    public Long getRentedBookId() {
        return rentedBookId;
    }

    public void setRentedBookId(Long rentedBookId) {
        this.rentedBookId = rentedBookId;
    }
}
