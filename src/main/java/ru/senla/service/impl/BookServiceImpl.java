package ru.senla.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.model.Book;
import ru.senla.repository.BookRepository;
import ru.senla.service.BookHistoryService;
import ru.senla.service.BookService;
import ru.senla.service.BookStorageService;
import ru.senla.service.RequestService;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;
    private BookHistoryService bookHistoryService;
    private BookStorageService bookStorageService;
    private RequestService requestService;

    public BookServiceImpl(BookRepository bookRepository,
                           BookHistoryService bookHistoryService,
                           BookStorageService bookStorageService,
                           RequestService requestService) {
        this.bookRepository = bookRepository;
        this.bookHistoryService = bookHistoryService;
        this.bookStorageService = bookStorageService;
        this.requestService = requestService;
    }

    @Override
    public void saveBook(Book book) {
        bookRepository.saveBook(book);
    }

    @Transactional
    @Override
    public void updateBookInfo(Long id, String name, Integer publicationYear, String author, String description) {
        Book book = bookRepository.getBookById(id);

        if (name != null) {
            book.setName(name);
        }
        if (publicationYear != null) {
            book.setPublicationYear(publicationYear);
        }
        if (author != null) {
            book.setAuthor(author);
        }
        if (description != null) {
            book.setDescription(description);
        }
    }

    @Transactional
    @Override
    public void deleteBookById(Long id) {
        Book bookToDelete = bookRepository.getBookById(id);

        bookHistoryService.deleteBookHistoriesByBookId(id);
        bookStorageService.deleteBookStorageByBookId(id);
        requestService.deleteRequestsByBookId(id);
        bookRepository.deleteBook(bookToDelete);
    }

    @Transactional
    @Override
    public Book getBookById(Long id) {
        Book book = bookRepository.getBookById(id);

        return new Book(book.getId(), book.getName(), book.getPublicationYear(), book.getAuthor(), book.getDescription());
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }
}
