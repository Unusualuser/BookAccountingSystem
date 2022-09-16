package ru.senla.repository.impl;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.exception.BookNotFoundException;
import ru.senla.model.Book;
import ru.senla.repository.BookRepository;

import java.util.List;

@Repository
@Transactional
public class BookRepositoryImpl implements BookRepository {
    private final static Logger LOGGER = Logger.getLogger(BookRepositoryImpl.class);
    private final SessionFactory sessionFactory;

    public BookRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveBook(Book book) {
        sessionFactory.getCurrentSession().save(book);
    }

    @Override
    public void deleteBook(Book book) {
        sessionFactory.getCurrentSession().delete(book);
    }

    @Override
    public Book getBookById(Long id) {
        Book book = sessionFactory.getCurrentSession().get(Book.class, id);

        if (book == null) {
            String errorMessage = String.format("Книга с id %d не найдена", id);
            LOGGER.error(errorMessage);
            throw new BookNotFoundException(errorMessage);
        }

        return book;
    }

    @Override
    public List<Book> getAllBooks() {
        return sessionFactory.getCurrentSession().createNativeQuery(
                 "SELECT book_id, name, publication_year, author, description " +
                    "FROM public.book", Book.class)
                .list();
    }
}
