package ru.senla.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.exception.BookServiceOperationException;
import ru.senla.model.Book;
import ru.senla.repository.BookHistoryRepository;
import ru.senla.repository.BookRepository;
import ru.senla.repository.BookStorageRepository;
import ru.senla.repository.RequestRepository;
import ru.senla.service.BookService;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final static Logger LOGGER = Logger.getLogger(BookServiceImpl.class);
    private final BookRepository bookRepository;
    private final BookHistoryRepository bookHistoryRepository;
    private final BookStorageRepository bookStorageRepository;
    private final RequestRepository requestRepository;

    public BookServiceImpl(BookRepository bookRepository, BookHistoryRepository bookHistoryRepository, BookStorageRepository bookStorageRepository, RequestRepository requestRepository) {
        this.bookRepository = bookRepository;
        this.bookHistoryRepository = bookHistoryRepository;
        this.bookStorageRepository = bookStorageRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public void saveBook(Book book) {
        try {
            this.bookRepository.saveBook(book);
        } catch (Exception e) {
            String errorMessage = "Ошибка при сохранении книги.";
            LOGGER.error(String.format("%s %s", errorMessage, e.getMessage()), e);
            LOGGER.debug(String.format("Книга: %s.", book.toString()));
            throw new BookServiceOperationException(errorMessage, e);
        }
    }

    @Transactional
    @Override
    public void updateBookInfo(Long id, String name, Integer publicationYear, String author, String description) {
        try {
            Book book = getBookById(id);
            if (name != null)
                book.setName(name);
            if (publicationYear != null)
                book.setPublicationYear(publicationYear);
            if (author != null)
                book.setAuthor(author);
            if (description != null)
                book.setDescription(description);
        } catch (Exception e) {
            String errorMessage = "Ошибка при обнновлении информации о книге.";
            LOGGER.error(String.format("%s %s", errorMessage, e.getMessage()), e);
            LOGGER.debug(String.format("Id книги: %d, новое название: %s, новый год публикации: %d, новый автор: %s, новое описание: %s.",
                                        id, name, publicationYear, author, description));
            throw new BookServiceOperationException(errorMessage, e);
        }
    }

    @Override
    public void deleteBookById(Long id) {
        try {
            this.bookHistoryRepository.deleteBookHistoriesByBookId(id);
            this.bookStorageRepository.deleteBookStoragesByBookId(id);
            this.requestRepository.deleteRequestsByBookId(id);
            this.bookRepository.deleteBookById(id);
        } catch (Exception e) {
            String errorMessage = "Ошибка при удалении книги.";
            LOGGER.error(String.format("%s %s", errorMessage, e.getMessage()), e);
            LOGGER.debug(String.format("Id книги: %d.", id));
            throw new BookServiceOperationException(errorMessage, e);
        }
    }

    @Override
    public Book getBookById(Long id) {
        try {
            return this.bookRepository.getBookById(id);
        } catch (Exception e) {
            String errorMessage = "Ошибка при получении книги.";
            LOGGER.error(String.format("%s %s", errorMessage, e.getMessage()), e);
            LOGGER.debug(String.format("Id книги: %d.", id));
            throw new BookServiceOperationException(errorMessage, e);
        }
    }

    @Override
    public List<Book> getAllBooks() {
        try {
            return this.bookRepository.getAllBooks();
        } catch (Exception e) {
            String errorMessage = "Ошибка при получении всех книг.";
            LOGGER.error(String.format("%s %s", errorMessage, e.getMessage()), e);
            throw new BookServiceOperationException(errorMessage, e);
        }
    }
}
