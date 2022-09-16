package ru.senla.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.dto.BookHistoryDto;
import ru.senla.dto.RentBookResponseDto;
import ru.senla.dto.ReturnRentedBookResponseDto;
import ru.senla.model.BookHistory;
import ru.senla.service.BookHistoryService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@Api(tags = "Методы для взаимодействия с сущностью BookHistory")
public class BookHistoryController {
    private final BookHistoryService bookHistoryService;

    public BookHistoryController(BookHistoryService bookHistoryService) {
        this.bookHistoryService = bookHistoryService;
    }

    @PostMapping("/rent-book")
    @ApiOperation(value = "Метод для аренды книги текущим пользователем по id книги")
    public ResponseEntity<?> rentBook(@ApiParam(value = "Идентификатор книги", example = "1", required = true)
                                      @Min(value = 0L, message = "Значение bookId должно быть положительным")
                                      @RequestParam
                                      Long bookId,
                                      @ApiIgnore Authentication authentication) {
        bookHistoryService.rentBook(bookId, authentication.getName());

        return new ResponseEntity<>(new RentBookResponseDto(bookId), HttpStatus.OK);
    }

    @PatchMapping("/moder/return-rented-book")
    @ApiOperation(value = "Метод для закрытия аренды книги по id истории книги")
    public ResponseEntity<?> returnRentedBook(@ApiParam(value = "Идентификатор истории книги", example = "1", required = true)
                                              @Min(value = 0L, message = "Значение bookHistoryId должно быть положительным")
                                              @RequestParam
                                              Long bookHistoryId) {
        bookHistoryService.returnRentedBook(bookHistoryId);

        return new ResponseEntity<>(new ReturnRentedBookResponseDto(bookHistoryId), HttpStatus.OK);
    }

    @GetMapping("/moder/full-book-history-by-book-id")
    @ApiOperation(value = "Метод для получения всей истории аренды книги по id книги")
    public ResponseEntity<?> getFullBookHistoryByBookId(@ApiParam(value = "Идентификатор книги", example = "1", required = true)
                                                        @Min(value = 0L, message = "Значение bookId должно быть положительным")
                                                        @RequestParam
                                                        Long bookId) {
        List<BookHistory> fullBookHistory = bookHistoryService.getFullBookHistoryByBookId(bookId);

        List<BookHistoryDto> fullBookHistoryDto = fullBookHistory.stream()
                                                                 .map(bookHistory -> new BookHistoryDto(bookHistory.getId(),
                                                                                                        bookHistory.getBook().getId(),
                                                                                                        bookHistory.getUser().getId(),
                                                                                                        bookHistory.getRentalDate(),
                                                                                                        bookHistory.getReturnDeadlineDate(),
                                                                                                        bookHistory.getReturnDate()))
                                                                 .collect(Collectors.toList());

        return new ResponseEntity<>(fullBookHistoryDto, HttpStatus.OK);
    }

    @GetMapping("/moder/book-histories-by-book-id-for-period")
    @ApiOperation(value = "Метод для получения истории аренды книги по id книги за период времени")
    public ResponseEntity<?> getBookHistoriesByBookIdForPeriod(@ApiParam(value = "Идентификатор книги", example = "1", required = true)
                                                               @Min(value = 0L, message = "Значение bookId должно быть положительным")
                                                               @RequestParam
                                                               Long bookId,
                                                               @ApiParam(value = "Дата начала периода", example = "2022-07-30", required = true)
                                                               @RequestParam
                                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                               LocalDate beginDate,
                                                               @ApiParam(value = "Дата конца периода", example = "2022-09-30", required = true)
                                                               @RequestParam
                                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                               LocalDate endDate) {
        List<BookHistory> bookHistories = bookHistoryService.getBookHistoriesByBookIdForPeriod(bookId, beginDate, endDate);
        List<BookHistoryDto> bookHistoriesDto = bookHistories.stream()
                                                             .map(bookHistory -> new BookHistoryDto(bookHistory.getId(),
                                                                                                    bookHistory.getBook().getId(),
                                                                                                    bookHistory.getUser().getId(),
                                                                                                    bookHistory.getRentalDate(),
                                                                                                    bookHistory.getReturnDeadlineDate(),
                                                                                                    bookHistory.getReturnDate()))
                                                             .collect(Collectors.toList());

        return new ResponseEntity<>(bookHistoriesDto, HttpStatus.OK);
    }

    @GetMapping("/moder/expired-rent")
    @ApiOperation(value = "Метод для получения просроченных аренд")
    public ResponseEntity<?> findAndGetExpiredRent() {
        List<BookHistory> expiredRents = bookHistoryService.findAndGetExpiredRent();
        List<BookHistoryDto> expiredRentsDto = expiredRents.stream()
                                                           .map(bookHistory -> new BookHistoryDto(bookHistory.getId(),
                                                                                                  bookHistory.getBook().getId(),
                                                                                                  bookHistory.getUser().getId(),
                                                                                                  bookHistory.getRentalDate(),
                                                                                                  bookHistory.getReturnDeadlineDate(),
                                                                                                  bookHistory.getReturnDate()))
                                                           .collect(Collectors.toList());

        return new ResponseEntity<>(expiredRentsDto, HttpStatus.OK);
    }
}
