package ru.senla.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.dto.BookDTO;
import ru.senla.dto.IdDTO;
import ru.senla.dto.SaveBookDTO;
import ru.senla.dto.UpdateBookInfoDTO;
import ru.senla.model.Book;
import ru.senla.service.BookService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/moder/book")
    public ResponseEntity<?> saveBook(@RequestBody SaveBookDTO saveBookDTO) {
        Book book = new Book(saveBookDTO.getName(),
                             saveBookDTO.getPublicationYear(),
                             saveBookDTO.getAuthor(),
                             saveBookDTO.getDescription());
        this.bookService.saveBook(book);
        return new ResponseEntity<>(new BookDTO(book.getId(),
                                                book.getName(),
                                                book.getPublicationYear(),
                                                book.getAuthor(),
                                                book.getDescription()),
                                    HttpStatus.OK);
    }

    @PatchMapping("/moder/book")
    public ResponseEntity<?> updateBookInfo(@RequestBody UpdateBookInfoDTO updateBookInfoDTO) {
        this.bookService.updateBookInfo(updateBookInfoDTO.getId(),
                                        updateBookInfoDTO.getName(),
                                        updateBookInfoDTO.getPublicationYear(),
                                        updateBookInfoDTO.getAuthor(),
                                        updateBookInfoDTO.getDescription());
        return new ResponseEntity<>(new BookDTO(updateBookInfoDTO.getId(),
                                                updateBookInfoDTO.getName(),
                                                updateBookInfoDTO.getPublicationYear(),
                                                updateBookInfoDTO.getAuthor(),
                                                updateBookInfoDTO.getDescription()),
                                    HttpStatus.OK);
    }

    @DeleteMapping("/moder/book/{id}")
    public ResponseEntity<?> deleteBookById(@PathVariable Long id) {
        this.bookService.deleteBookById(id);
        return new ResponseEntity<>(new IdDTO(id), HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/book/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        Book book = this.bookService.getBookById(id);
        return new ResponseEntity<>(new BookDTO(book.getId(),
                                                book.getName(),
                                                book.getPublicationYear(),
                                                book.getAuthor(),
                                                book.getDescription()),
                                    HttpStatus.OK);
    }

    @GetMapping("/all-books")
    public ResponseEntity<?> getAllBooks() {
        List<Book> allBooks = this.bookService.getAllBooks();
        List<BookDTO> allBooksDTO = allBooks.stream()
                                            .map(book -> new BookDTO(book.getId(),
                                                                     book.getName(),
                                                                     book.getPublicationYear(),
                                                                     book.getAuthor(),
                                                                     book.getDescription()))
                                            .collect(Collectors.toList());
        return new ResponseEntity<>(allBooksDTO, HttpStatus.OK);
    }
}
