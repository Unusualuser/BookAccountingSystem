package ru.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.example.dto.BookDto;
import ru.example.dto.IdDto;
import ru.example.dto.SaveBookRequestDto;
import ru.example.dto.UpdateBookInfoRequestDto;
import ru.example.model.Book;
import ru.example.service.BookService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@Api(tags = "Методы для взаимодействия с сущностью Book")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/moder/book")
    @ApiOperation(value = "Метод для добавления новой книги")
    public ResponseEntity<?> saveBook(@ApiParam(value = "Модель запроса SaveBookRequestDto", required = true)
                                      @Valid
                                      @RequestBody
                                      SaveBookRequestDto saveBookRequestDto) {
        Book book = new Book(saveBookRequestDto.getName(),
                             saveBookRequestDto.getPublicationYear(),
                             saveBookRequestDto.getAuthor(),
                             saveBookRequestDto.getDescription());
        bookService.saveBook(book);

        return new ResponseEntity<>(new BookDto(book.getId(),
                                                book.getName(),
                                                book.getPublicationYear(),
                                                book.getAuthor(),
                                                book.getDescription()),
                                    HttpStatus.OK);
    }

    @PatchMapping("/moder/book")
    @ApiOperation(value = "Метод для обновления информации о книге")
    public ResponseEntity<?> updateBookInfo(@ApiParam(value = "Модель запроса UpdateBookInfoRequestDto", required = true)
                                            @Valid
                                            @RequestBody
                                            UpdateBookInfoRequestDto updateBookInfoRequestDto) {
        bookService.updateBookInfo(updateBookInfoRequestDto.getId(),
                                        updateBookInfoRequestDto.getName(),
                                        updateBookInfoRequestDto.getPublicationYear(),
                                        updateBookInfoRequestDto.getAuthor(),
                                        updateBookInfoRequestDto.getDescription());

        return new ResponseEntity<>(new BookDto(updateBookInfoRequestDto.getId(),
                                                updateBookInfoRequestDto.getName(),
                                                updateBookInfoRequestDto.getPublicationYear(),
                                                updateBookInfoRequestDto.getAuthor(),
                                                updateBookInfoRequestDto.getDescription()),
                                    HttpStatus.OK);
    }

    @DeleteMapping("/moder/book/{id}")
    @ApiOperation(value = "Метод для удаления книги по id")
    public ResponseEntity<?> deleteBookById(@ApiParam(value = "Идентификатор книги", example = "1", required = true)
                                            @Min(value = 0L, message = "Значение id должно быть положительным")
                                            @PathVariable
                                            Long id) {
        bookService.deleteBookById(id);

        return new ResponseEntity<>(new IdDto(id), HttpStatus.OK);
    }

    @GetMapping("/book/{id}")
    @ApiOperation(value = "Метод для получения книги по id")
    public ResponseEntity<?> getBookById(@ApiParam(value = "Идентификатор книги", example = "1", required = true)
                                         @Min(value = 0L, message = "Значение id должно быть положительным")
                                         @PathVariable
                                         Long id) {
        Book book = bookService.getBookById(id);

        return new ResponseEntity<>(new BookDto(book.getId(),
                                                book.getName(),
                                                book.getPublicationYear(),
                                                book.getAuthor(),
                                                book.getDescription()),
                                    HttpStatus.OK);
    }

    @GetMapping("/all-books")
    @ApiOperation(value = "Метод для получения всех книг")
    public ResponseEntity<?> getAllBooks() {
        List<Book> allBooks = bookService.getAllBooks();
        List<BookDto> allBooksDto = allBooks.stream()
                                            .map(book -> new BookDto(book.getId(),
                                                                     book.getName(),
                                                                     book.getPublicationYear(),
                                                                     book.getAuthor(),
                                                                     book.getDescription()))
                                            .collect(Collectors.toList());

        return new ResponseEntity<>(allBooksDto, HttpStatus.OK);
    }
}
