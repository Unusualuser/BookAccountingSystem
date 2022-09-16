package ru.senla.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.exception.BookNotFoundException;
import ru.senla.exception.BookServiceOperationException;
import ru.senla.model.Book;
import ru.senla.repository.BookRepository;
import ru.senla.service.BookHistoryService;
import ru.senla.service.BookService;
import ru.senla.service.BookStorageService;
import ru.senla.service.RequestService;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final static Logger LOGGER = Logger.getLogger(BookServiceImpl.class);
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
        try {
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
        } catch (ObjectNotFoundException e) {
            String errorMessage = String.format("Книга с id %d не найдена", id);
            LOGGER.debug(String.format("%s %s", "Ошибка при обновлении информации о книге.", e.getMessage()), e);
            LOGGER.error(errorMessage);
            throw new BookNotFoundException(errorMessage);
        }
    }

    @Transactional
    @Override
    public void deleteBookById(Long id) {
        try {
            bookHistoryService.deleteBookHistoriesByBookId(id);
            bookStorageService.deleteBookStorageByBookId(id);
            requestService.deleteRequestsByBookId(id);
            bookRepository.deleteBookById(id);
        } catch (ObjectNotFoundException e) {
            String errorMessage = String.format("Книга с id %d не найдена", id);
            LOGGER.debug(String.format("%s %s", "Ошибка при удалении книги.", e.getMessage()), e);
            LOGGER.error(errorMessage);
            throw new BookNotFoundException(errorMessage);
        }
    }

    @Transactional
    @Override
    public Book getBookById(Long id) {
        try {
            Book book = bookRepository.getBookById(id);
            return new Book(book.getId(), book.getName(), book.getPublicationYear(), book.getAuthor(), book.getDescription());
        } catch (ObjectNotFoundException e) {
            String errorMessage = String.format("Книга с id %d не найдена", id);
            LOGGER.debug(String.format("%s %s", "Ошибка при получении книги.", e.getMessage()), e);
            LOGGER.error(errorMessage);
            throw new BookNotFoundException(errorMessage);
        }
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    @Override
    public void throwBookNotFoundExceptionIfBookByIdNotContains(Long bookId, String debugMessage) {
        if (!bookRepository.containsById(bookId)) {
            String errorMessage = String.format("Книга с id %d не найдена", bookId);
            LOGGER.debug(debugMessage);
            LOGGER.error(errorMessage);
            throw new BookNotFoundException(errorMessage);
        }
    }
}
