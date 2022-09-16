package ru.senla.repository.impl;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.exception.BookStorageIllegalReduceQuantityException;
import ru.senla.model.BookStorage;
import ru.senla.repository.BookStorageRepository;

import java.util.List;

@Repository
@Transactional
public class BookStorageRepositoryImpl implements BookStorageRepository {
    private final SessionFactory sessionFactory;

    public BookStorageRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addQuantityByBookId(Long id, Long additionalQuantity) {
        BookStorage bookStorage = getBookStorageByBookId(id);

        bookStorage.setQuantity(bookStorage.getQuantity() + additionalQuantity);
    }

    @Override
    public void reduceQuantityByBookId(Long id, Long quantityToBeReduce) {
        BookStorage bookStorage = getBookStorageByBookId(id);
        boolean isQuantityGreaterThanQuantityToBeReduce = (bookStorage.getQuantity() - quantityToBeReduce) >= 0;

        if (!isQuantityGreaterThanQuantityToBeReduce) {
            throw new BookStorageIllegalReduceQuantityException(id);
        }

        bookStorage.setQuantity(bookStorage.getQuantity() - quantityToBeReduce);
    }

    @Override
    public void incrementQuantityByBookId(Long id) {
        Long additionalQuantity = 1L;

        addQuantityByBookId(id, additionalQuantity);
    }

    @Override
    public void decrementQuantityByBookId(Long id) {
        Long quantityToBeReduce = 1L;

        reduceQuantityByBookId(id, quantityToBeReduce);
    }

    @Override
    public Long getQuantityByBookId(Long id) {
        return getBookStorageByBookId(id).getQuantity();
    }

    @Override
    public BookStorage getBookStorageByBookId(Long id) {
        List<BookStorage> bookStorageList = sessionFactory.getCurrentSession().createNativeQuery(
                 "SELECT book_storage_id, book_id, quantity " +
                    "FROM public.book_storage " +
                    "WHERE book_id = :id", BookStorage.class)
                .setParameter("id", id)
                .list();

        if (bookStorageList.size() != 1) {
            throw new ObjectNotFoundException(
                    BookStorage.class,
                    String.format("Записи BookStorage c book_id %d не найдено.", id)
            );
        }

        return bookStorageList.get(0);
    }

    @Override
    public void deleteBookStorageByBookId(Long bookId) {
        sessionFactory.getCurrentSession().createNativeQuery(
                 "DELETE FROM public.book_storage " +
                    "WHERE book_id = :bookId")
                .setParameter("bookId", bookId)
                .executeUpdate();
    }
}
