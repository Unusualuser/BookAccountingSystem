package ru.senla.repository.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.model.BookHistory;
import ru.senla.repository.BookHistoryRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public class BookHistoryRepositoryImpl implements BookHistoryRepository {
    private final SessionFactory sessionFactory;

    public BookHistoryRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveBookHistory(BookHistory bookHistory) {
        this.sessionFactory.getCurrentSession().save(bookHistory);
    }

    @Override
    public void setReturnDateById(Long id, LocalDate returnDate) {
        this.sessionFactory.getCurrentSession().createNativeQuery(
                 "UPDATE public.book_history " +
                    "SET return_date = :returnDate " +
                    "WHERE book_history_id = :id")
                .setParameter("returnDate", returnDate)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public List<BookHistory> getFullBookHistoryByBookId(Long id) {
        return this.sessionFactory.getCurrentSession().createNativeQuery(
                         "SELECT book_history_id, book_id, user_id, rental_date, return_deadline_date, return_date " +
                            "FROM public.book_history " +
                            "WHERE book_id = :id", BookHistory.class)
                .setParameter("id", id)
                .list();
    }

    @Override
    public List<BookHistory> getBookHistoriesByBookIdForPeriod(Long id, LocalDate beginDate, LocalDate endDate) {
        return this.sessionFactory.getCurrentSession().createNativeQuery(
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
        return this.sessionFactory.getCurrentSession().load(BookHistory.class, id);
    }

    @Override
    public void deleteBookHistoriesByBookId(Long bookId) {
        this.sessionFactory.getCurrentSession().createNativeQuery(
                 "DELETE FROM public.book_history " +
                    "WHERE book_id = :bookId")
                .setParameter("bookId", bookId)
                .executeUpdate();
    }

    @Override
    public List<BookHistory> findAndGetExpiredRent() {
        LocalDate localDateNow = LocalDate.now();
        return this.sessionFactory.getCurrentSession().createNativeQuery(
                 "SELECT book_history_id, book_id, user_id, rental_date, return_deadline_date, return_date " +
                    "FROM public.book_history " +
                    "WHERE return_date IS NULL " +
                    "AND return_deadline_date < :localDateNow", BookHistory.class)
                .setParameter("localDateNow", localDateNow)
                .list();
    }
}
