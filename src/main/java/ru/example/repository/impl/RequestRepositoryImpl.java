package ru.example.repository.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.example.model.Request;
import ru.example.model.fieldenum.RequestStatus;
import ru.example.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class RequestRepositoryImpl implements RequestRepository {
    private final SessionFactory sessionFactory;

    public RequestRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveRequest(Request request) {
        this.sessionFactory.getCurrentSession().save(request);
    }

    @Override
    public List<Request> getRequestsByBookIdForPeriod(Long id, LocalDateTime beginDttm, LocalDateTime endDttm) {
        return sessionFactory.getCurrentSession().createNativeQuery(
                 "SELECT request_id, book_id, user_id, create_dttm, request_status " +
                    "FROM public.request " +
                    "WHERE book_id = :id " +
                    "AND create_dttm > :beginDttm " +
                    "AND create_dttm < :endDttm", Request.class)
                .setParameter("id", id)
                .setParameter("beginDttm", beginDttm)
                .setParameter("endDttm", endDttm)
                .list();
    }

    @Override
    public List<Request> closeBatchRequestsByBookIdAndBatch(Long bookId, Long batch) {
        List<Request> batchRequests = sessionFactory.getCurrentSession().createNativeQuery(
                 "SELECT request_id, book_id, user_id, create_dttm, request_status " +
                    "FROM public.request " +
                    "WHERE book_id = :bookId " +
                        "AND request_status NOT LIKE 'DONE'" +
                    "ORDER BY create_dttm " +
                    "LIMIT :batch", Request.class)
                .setParameter("batch", batch)
                .setParameter("bookId", bookId)
                .list();

        for (Request request : batchRequests) {
            request.setRequestStatus(RequestStatus.DONE);
        }

        return batchRequests;
    }

    @Override
    public List<Request> getAllRequestsByBookId(Long bookId) {
        return sessionFactory.getCurrentSession().createNativeQuery(
                 "SELECT request_id, book_id, user_id, create_dttm, request_status " +
                    "FROM public.request " +
                    "WHERE book_id = :id ", Request.class)
                .setParameter("id", bookId)
                .list();
    }

    @Override
    public void deleteRequestsByBookId(Long bookId) {
        sessionFactory.getCurrentSession().createNativeQuery(
                 "DELETE FROM public.request " +
                    "WHERE book_id = :bookId")
                .setParameter("bookId", bookId)
                .executeUpdate();
    }
}
