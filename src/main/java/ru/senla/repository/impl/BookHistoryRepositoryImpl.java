package ru.senla.repository.impl;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.exception.BookHistoryNotFoundException;
import ru.senla.model.BookHistory;
import ru.senla.repository.BookHistoryRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public class BookHistoryRepositoryImpl implements BookHistoryRepository {
    private final static Logger LOGGER = Logger.getLogger(BookHistoryRepositoryImpl.class);
    private final SessionFactory sessionFactory;

    public BookHistoryRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveBookHistory(BookHistory bookHistory) {
        sessionFactory.getCurrentSession().save(bookHistory);
    }

    @Override
    public List<BookHistory> getFullBookHistoryByBookId(Long id) {
        return sessionFactory.getCurrentSession().createNativeQuery(
                         "SELECT book_history_id, book_id, user_id, rental_date, return_deadline_date, return_date " +
                            "FROM public.book_history " +
                            "WHERE book_id = :id", BookHistory.class)
                .setParameter("id", id)
                .list();
    }

    @Override
    public List<BookHistory> getBookHistoriesByBookIdForPeriod(Long id, LocalDate beginDate, LocalDate endDate) {
        return sessionFactory.getCurrentSession().createNativeQuery(
                 "SELECT book_history_id, book_id, user_id, rental_date, return_deadline_date, return_date " +
                    "FROM public.book_history " +
                    "WHERE book_id = :id " +
                    "AND rental_date > :beginDate " +
                    "AND rental_date < :endDate", BookHistory.class)
                .setParameter("id", id)
                .setParameter("beginDate", beginDate)
                .setParameter("endDate", endDate)
                .list();
    }

    @Override
    public BookHistory getBookHistoryById(Long id) {
        BookHistory bookHistory = sessionFactory.getCurrentSession().get(BookHistory.class, id);

        if (bookHistory == null) {
            String errorMessage = String.format("Запись истории аренды с id %d не найдена.", id);
            LOGGER.error(errorMessage);
            throw new BookHistoryNotFoundException(errorMessage);
        }

        return bookHistory;
    }

    @Override
    public void deleteBookHistoriesByBookId(Long bookId) {
        sessionFactory.getCurrentSession().createNativeQuery(
                 "DELETE FROM public.book_history " +
                    "WHERE book_id = :bookId")
                .setParameter("bookId", bookId)
                .executeUpdate();
    }

    @Override
    public List<BookHistory> findAndGetExpiredRent() {
        LocalDate localDateNow = LocalDate.now();

        return sessionFactory.getCurrentSession().createNativeQuery(
                 "SELECT book_history_id, book_id, user_id, rental_date, return_deadline_date, return_date " +
                    "FROM public.book_history " +
                    "WHERE return_date IS NULL " +
                    "AND return_deadline_date < :localDateNow", BookHistory.class)
                .setParameter("localDateNow", localDateNow)
                .list();
    }
}
