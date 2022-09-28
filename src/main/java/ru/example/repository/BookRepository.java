package ru.example.repository;

import ru.example.model.Book;

import java.util.List;

public interface BookRepository {
    void saveBook(Book book);

    void deleteBook(Book book);

    Book getBookById(Long id);

    List<Book> getAllBooks();
}
