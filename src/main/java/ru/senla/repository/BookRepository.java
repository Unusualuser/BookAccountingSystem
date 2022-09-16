package ru.senla.repository;

import ru.senla.model.Book;

import java.util.List;

public interface BookRepository {
    void saveBook(Book book);

    void deleteBook(Book book);

    Book getBookById(Long id);

    List<Book> getAllBooks();
}
