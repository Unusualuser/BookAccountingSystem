package ru.senla.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDate;

public class BookHistoryDto implements Serializable {
    private Long id;
    private Long bookId;
    private Long userId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate rentalDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDeadlineDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;

    public BookHistoryDto() {
    }

    public BookHistoryDto(Long id,
                          Long bookId,
                          Long userId,
                          LocalDate rentalDate,
                          LocalDate returnDeadlineDate,
                          LocalDate returnDate) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.rentalDate = rentalDate;
        this.returnDeadlineDate = returnDeadlineDate;
        this.returnDate = returnDate;
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

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDate getReturnDeadlineDate() {
        return returnDeadlineDate;
    }

    public void setReturnDeadlineDate(LocalDate returnDeadlineDate) {
        this.returnDeadlineDate = returnDeadlineDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
