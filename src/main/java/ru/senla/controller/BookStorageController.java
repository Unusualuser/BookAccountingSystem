package ru.senla.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.dto.AddQuantityByBookIdRequestDTO;
import ru.senla.dto.AddQuantityByBookIdResponseDTO;
import ru.senla.dto.GetQuantityByBookIdDTO;
import ru.senla.dto.ReduceQuantityByBookIdRequestDTO;
import ru.senla.dto.ReduceQuantityByBookIdResponseDTO;
import ru.senla.service.BookStorageService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@Validated
public class BookStorageController {
    private final BookStorageService bookStorageService;

    public BookStorageController(BookStorageService bookStorageService) {
        this.bookStorageService = bookStorageService;
    }

    @PatchMapping("/moder/add-quantity-by-book-id")
    public ResponseEntity<?> addQuantityByBookId(@Valid @RequestBody AddQuantityByBookIdRequestDTO addQuantityByBookIdRequestDTO) {
        Long bookId = addQuantityByBookIdRequestDTO.getBookId();
        Long quantityToAdd = addQuantityByBookIdRequestDTO.getQuantityToAdd();

        this.bookStorageService.addQuantityByBookIdCloseRequestsIfExistsAndNotifyUsers(bookId, quantityToAdd);
        return new ResponseEntity<>(new AddQuantityByBookIdResponseDTO(bookId, quantityToAdd), HttpStatus.OK);
    }

    @PatchMapping("/moder/reduce-quantity-by-book-id")
    public ResponseEntity<?> reduceQuantityByBookId(@Valid @RequestBody ReduceQuantityByBookIdRequestDTO reduceQuantityByBookIdRequestDTO) {
        Long bookId = reduceQuantityByBookIdRequestDTO.getBookId();
        Long quantityToReduce = reduceQuantityByBookIdRequestDTO.getQuantityToReduce();

        this.bookStorageService.reduceQuantityByBookId(bookId, quantityToReduce);
        return new ResponseEntity<>(new ReduceQuantityByBookIdResponseDTO(bookId, quantityToReduce), HttpStatus.OK);
    }

    @GetMapping("/quantity-by-book-id/{bookId}")
    public ResponseEntity<?> getQuantityByBookId(@Min(value = 0L, message = "Значение bookId должно быть положительным")
                                                 @PathVariable Long bookId) {
        Long quantity = this.bookStorageService.getQuantityByBookId(bookId);
        return new ResponseEntity<>(new GetQuantityByBookIdDTO(bookId, quantity), HttpStatus.OK);
    }
}
