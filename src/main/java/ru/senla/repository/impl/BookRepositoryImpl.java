package ru.senla.repository.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.model.Book;
import ru.senla.repository.BookRepository;

import java.util.List;

@Repository
@Transactional
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    public BookRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveBook(Book book) {
        this.sessionFactory.getCurrentSession().save(book);
    }

    @Override
    public void deleteBookById(Long id) {
        Book bookToDelete = getBookById(id);
        this.sessionFactory.getCurrentSession().delete(bookToDelete);
    }

    @Override
    public Book getBookById(Long id) {
        return this.sessionFactory.getCurrentSession().load(Book.class, id);
    }

    @Override
    public List<Book> getAllBooks() {
        return this.sessionFactory.getCurrentSession().createNativeQuery(
                 "SELECT book_id, name, publication_year, author, description " +
                    "FROM public.book", Book.class)
                .list();
    }
}
