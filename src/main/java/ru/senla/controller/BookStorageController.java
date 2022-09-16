package ru.senla.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.dto.AddQuantityByBookIdRequestDto;
import ru.senla.dto.AddQuantityByBookIdResponseDto;
import ru.senla.dto.GetQuantityByBookIdResponseDto;
import ru.senla.dto.ReduceQuantityByBookIdRequestDto;
import ru.senla.dto.ReduceQuantityByBookIdResponseDto;
import ru.senla.service.BookStorageService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@Validated
@Api(tags = "Методы для взаимодействия с сущностью BookStorage")
public class BookStorageController {
    private final BookStorageService bookStorageService;

    public BookStorageController(BookStorageService bookStorageService) {
        this.bookStorageService = bookStorageService;
    }

    @PatchMapping("/moder/add-quantity")
    @ApiOperation(value = "Метод для добавления количества книг в хранилище")
    public ResponseEntity<?> addQuantity(@ApiParam(value = "Модель запроса AddQuantityByBookIdRequestDto", required = true)
                                         @Valid
                                         @RequestBody
                                         AddQuantityByBookIdRequestDto addQuantityByBookIdRequestDto) {
        Long bookId = addQuantityByBookIdRequestDto.getBookId();
        Long quantityToAdd = addQuantityByBookIdRequestDto.getQuantityToAdd();

        bookStorageService.addQuantityByBookIdCloseRequestsIfExistsAndNotifyUsers(bookId, quantityToAdd);

        return new ResponseEntity<>(new AddQuantityByBookIdResponseDto(bookId, quantityToAdd), HttpStatus.OK);
    }

    @PatchMapping("/moder/reduce-quantity")
    @ApiOperation(value = "Метод для уменьшения количества книг в хранилище")
    public ResponseEntity<?> reduceQuantity(@ApiParam(value = "Модель запроса ReduceQuantityByBookIdRequestDto", required = true)
                                            @Valid
                                            @RequestBody
                                            ReduceQuantityByBookIdRequestDto reduceQuantityByBookIdRequestDto) {
        Long bookId = reduceQuantityByBookIdRequestDto.getBookId();
        Long quantityToReduce = reduceQuantityByBookIdRequestDto.getQuantityToReduce();

        bookStorageService.reduceQuantityByBookId(bookId, quantityToReduce);

        return new ResponseEntity<>(new ReduceQuantityByBookIdResponseDto(bookId, quantityToReduce), HttpStatus.OK);
    }

    @GetMapping("/quantity-by-book-id/{bookId}")
    @ApiOperation(value = "Метод для получения количества книг в хранилище по id книги")
    public ResponseEntity<?> getQuantityByBookId(@ApiParam(value = "Идентификатор книги", required = true)
                                                 @Min(value = 0L, message = "Значение bookId должно быть положительным")
                                                 @PathVariable
                                                 Long bookId) {
        Long quantity = bookStorageService.getQuantityByBookId(bookId);

        return new ResponseEntity<>(new GetQuantityByBookIdResponseDto(bookId, quantity), HttpStatus.OK);
    }
}
