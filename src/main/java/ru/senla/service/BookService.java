package ru.senla.service;

import ru.senla.model.Book;

import java.util.List;

public interface BookService {
    void saveBook(Book book);

    void updateBookInfo(Long id, String name, Integer publicationYear, String author, String description);

    void deleteBookById(Long id);

    Book getBookById(Long id);

    List<Book> getAllBooks();
}
