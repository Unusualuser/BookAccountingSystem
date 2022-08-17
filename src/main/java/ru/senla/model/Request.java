package ru.senla.model;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import ru.senla.model.fieldenum.RequestStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "request", schema = "public")
public class Request implements Serializable {
    @Id
    @SequenceGenerator(name = "request_seq", sequenceName = "request_request_id_seq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "request_seq")
    @Column(name = "request_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "create_dttm", nullable = false)
    private LocalDateTime createDttm;

    @Column(name = "request_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    @PrePersist
    protected void onCreate() {
        this.createDttm = LocalDateTime.now();
        this.requestStatus = RequestStatus.NEW;
    }

    public Request() {
    }

    public Request(Long id, Book book, User user, LocalDateTime createDttm, RequestStatus requestStatus) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.createDttm = createDttm;
        this.requestStatus = requestStatus;
    }

    public Book getBook() {
        return book;
    }

    public LocalDateTime getCreateDttm() {
        return createDttm;
    }

    public Long getId() {
        return id;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setCreateDttm(LocalDateTime createDttm) {
        this.createDttm = createDttm;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void completeRequest() {
        setRequestStatus(RequestStatus.DONE);
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        if (this.requestStatus == RequestStatus.DONE && requestStatus == RequestStatus.DONE)
            throw new UnsupportedOperationException(String.format("Запрос с id %d уже выполнен.", this.id));

        this.requestStatus = requestStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request)) return false;
        Request request = (Request) o;
        if (o instanceof HibernateProxy) {
            request = (Request) Hibernate.unproxy(o);
        }
        return Objects.equals(id, request.id) && book.equals(request.book) && createDttm.equals(request.createDttm) && requestStatus == request.getRequestStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, createDttm, requestStatus);
    }
}
