package ru.senla.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.dto.BookHistoryDTO;
import ru.senla.dto.RentBookResponseDTO;
import ru.senla.dto.ReturnRentedBookResponseDTO;
import ru.senla.model.BookHistory;
import ru.senla.service.BookHistoryService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookHistoryController {
    private final BookHistoryService bookHistoryService;

    public BookHistoryController(BookHistoryService bookHistoryService) {
        this.bookHistoryService = bookHistoryService;
    }

    @PostMapping("/rent-book")
    public ResponseEntity<?> rentBook(@RequestParam Long bookId, Authentication authentication) {
        this.bookHistoryService.rentBook(bookId, authentication.getName());
        return new ResponseEntity<>(new RentBookResponseDTO(bookId), HttpStatus.OK);
    }

    @PatchMapping("/moder/return-rented-book")
    public ResponseEntity<?> returnRentedBook(@RequestParam Long bookHistoryId) {
        this.bookHistoryService.returnRentedBook(bookHistoryId);
        return new ResponseEntity<>(new ReturnRentedBookResponseDTO(bookHistoryId), HttpStatus.OK);
    }

    @GetMapping("/moder/full-book-history-by-book-id")
    public ResponseEntity<?> getFullBookHistoryByBookId(@RequestParam Long bookId) {
        List<BookHistory> fullBookHistory = this.bookHistoryService.getFullBookHistoryByBookId(bookId);
        List<BookHistoryDTO> fullBookHistoryDTO = fullBookHistory.stream()
             .map(bookHistory -> new BookHistoryDTO(
                                                    bookHistory.getId(),
                                                    bookHistory.getBook().getId(),
                                                    bookHistory.getUser().getId(),
                                                    bookHistory.getRentalDate(),
                                                    bookHistory.getReturnDeadlineDate(),
                                                    bookHistory.getReturnDate()))
             .collect(Collectors.toList());
        return new ResponseEntity<>(fullBookHistoryDTO, HttpStatus.OK);
    }

    @GetMapping("/moder/book-histories-by-book-id-for-period")
    public ResponseEntity<?> getBookHistoriesByBookIdForPeriod(@RequestParam Long bookId,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beginDate,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<BookHistory> bookHistories = this.bookHistoryService.getBookHistoriesByBookIdForPeriod(bookId,
                                                                                                    beginDate,
                                                                                                    endDate);
        List<BookHistoryDTO> bookHistoriesDTO = bookHistories.stream()
             .map(bookHistory -> new BookHistoryDTO(
                                                     bookHistory.getId(),
                                                     bookHistory.getBook().getId(),
                                                     bookHistory.getUser().getId(),
                                                     bookHistory.getRentalDate(),
                                                     bookHistory.getReturnDeadlineDate(),
                                                     bookHistory.getReturnDate()))
             .collect(Collectors.toList());
        return new ResponseEntity<>(bookHistoriesDTO, HttpStatus.OK);
    }

    @GetMapping("/moder/expired-rent")
    public ResponseEntity<?> findAndGetExpiredRent() {
        List<BookHistory> expiredRents = this.bookHistoryService.findAndGetExpiredRent();
        List<BookHistoryDTO> expiredRentsDTO = expiredRents.stream()
                .map(bookHistory -> new BookHistoryDTO(
                                                       bookHistory.getId(),
                                                       bookHistory.getBook().getId(),
                                                       bookHistory.getUser().getId(),
                                                       bookHistory.getRentalDate(),
                                                       bookHistory.getReturnDeadlineDate(),
                                                       bookHistory.getReturnDate()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(expiredRentsDTO, HttpStatus.OK);
    }
}
