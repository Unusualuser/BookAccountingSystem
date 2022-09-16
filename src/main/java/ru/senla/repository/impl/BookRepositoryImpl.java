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
        sessionFactory.getCurrentSession().save(book);
    }

    @Override
    public void deleteBookById(Long id) {
        Book bookToDelete = getBookById(id);

        sessionFactory.getCurrentSession().delete(bookToDelete);
    }

    @Override
    public Book getBookById(Long id) {
        return sessionFactory.getCurrentSession().load(Book.class, id);
    }

    @Override
    public List<Book> getAllBooks() {
        return sessionFactory.getCurrentSession().createNativeQuery(
                 "SELECT book_id, name, publication_year, author, description " +
                    "FROM public.book", Book.class)
                .list();
    }

    @Override
    public boolean containsById(Long id) {
        return sessionFactory.getCurrentSession().get(Book.class, id) != null;
    }
}
