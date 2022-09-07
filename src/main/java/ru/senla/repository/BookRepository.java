package ru.senla.repository;

import ru.senla.model.Book;

import java.util.List;

public interface BookRepository {
    void saveBook(Book book);

    void deleteBookById(Long id);

    Book getBookById(Long id);

    List<Book> getAllBooks();
}
