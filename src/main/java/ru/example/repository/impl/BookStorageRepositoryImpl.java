package ru.example.repository.impl;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.example.exception.BookStorageIllegalReduceQuantityException;
import ru.example.exception.BookStorageNotFoundException;
import ru.example.model.BookStorage;
import ru.example.repository.BookStorageRepository;

import java.util.List;

@Repository
@Transactional
public class BookStorageRepositoryImpl implements BookStorageRepository {
    private final static Logger LOGGER = Logger.getLogger(BookStorageRepositoryImpl.class);
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
            LOGGER.debug("Ошибка при уменьшении количества книг.");
            throw new BookStorageIllegalReduceQuantityException(id);
        }

        bookStorage.setQuantity(bookStorage.getQuantity() - quantityToBeReduce);
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
            LOGGER.debug("Ошибка при получении хранилища книг по id.");
            throw new BookStorageNotFoundException(String.format("Записи BookStorage c book_id %d не найдено.", id));
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
