package ru.senla.repository.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.model.BookStorage;
import ru.senla.repository.BookStorageRepository;

import javax.persistence.EntityNotFoundException;
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
        this.sessionFactory.getCurrentSession().createNativeQuery(
                 "UPDATE public.book_storage " +
                    "SET quantity = book_storage.quantity + :additionalQuantity " +
                    "WHERE book_id = :id")
                .setParameter("additionalQuantity", additionalQuantity)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void reduceQuantityByBookId(Long id, Long quantityToBeReduce) {
        boolean isQuantityGreaterThanQuantityToBeReduce = (getQuantityByBookId(id) - quantityToBeReduce) >= 0;

        if (!isQuantityGreaterThanQuantityToBeReduce)
            throw new UnsupportedOperationException(String.format("Невозможно уменьшить количество книги с id %d, так как её количества недостаточно.", id));

        this.sessionFactory.getCurrentSession().createNativeQuery(
                        "UPDATE public.book_storage " +
                                "SET quantity = book_storage.quantity - :quantityToBeReduce " +
                                "WHERE book_id = :id")
                .setParameter("quantityToBeReduce", quantityToBeReduce)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void incrementQuantityByBookId(Long id) {
        Long additionalQuantity = 1L;
        this.addQuantityByBookId(id, additionalQuantity);
    }

    @Override
    public void decrementQuantityByBookId(Long id) {
        Long quantityToBeReduce = 1L;
        this.reduceQuantityByBookId(id, quantityToBeReduce);
    }

    @Override
    public Long getQuantityByBookId(Long id) {
        List<BookStorage> bookStorageList = this.sessionFactory.getCurrentSession().createNativeQuery(
                 "SELECT book_storage_id, book_id, quantity " +
                    "FROM public.book_storage " +
                    "WHERE book_id = :id", BookStorage.class)
                .setParameter("id", id)
                .list();

        if (bookStorageList.size() != 1)
            throw new EntityNotFoundException("Книга с данным id не найдена в хранилище.");

        return bookStorageList.get(0).getQuantity();
    }

    @Override
    public void deleteBookStoragesByBookId(Long bookId) {
        this.sessionFactory.getCurrentSession().createNativeQuery(
                 "DELETE FROM public.book_storage " +
                    "WHERE book_id = :bookId")
                .setParameter("bookId", bookId)
                .executeUpdate();
    }
}
