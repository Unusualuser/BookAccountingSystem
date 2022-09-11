package ru.senla.model;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "book_history", schema = "public")
public class BookHistory implements Serializable {
    @Id
    @SequenceGenerator(name = "book_history_seq", sequenceName = "book_history_book_history_id_seq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_history_seq")
    @Column(name = "book_history_id", nullable = false)
    @Min(value = 0L, message = "Значение id должно быть положительным")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "rental_date", nullable = false)
    private LocalDate rentalDate;

    @Column(name = "return_deadline_date", nullable = false)
    private LocalDate returnDeadlineDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @PrePersist
    protected void onCreate() {
        if (this.rentalDate == null) {
            this.rentalDate = LocalDate.now();
        }
    }

    public BookHistory() {
    }

    public BookHistory(Book book, User user, LocalDate rentalDate, LocalDate returnDeadlineDate) {
        this.book = book;
        this.user = user;
        this.rentalDate = rentalDate;
        this.returnDeadlineDate = returnDeadlineDate;
    }

    public BookHistory(Long id, Book book, User user, LocalDate rentalDate, LocalDate returnDeadlineDate, LocalDate returnDate) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.rentalDate = rentalDate;
        this.returnDeadlineDate = returnDeadlineDate;
        this.returnDate = returnDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LocalDate getReturnDeadlineDate() {
        return returnDeadlineDate;
    }

    public void setReturnDeadlineDate(LocalDate returnDeadlineDate) {
        this.returnDeadlineDate = returnDeadlineDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookHistory)) return false;
        BookHistory that = (BookHistory) o;
        if (o instanceof HibernateProxy) {
            that = (BookHistory) Hibernate.unproxy(o);
        }
        return Objects.equals(id, that.id)
                && Objects.equals(book, that.book)
                && Objects.equals(user, that.user)
                && Objects.equals(rentalDate, that.rentalDate)
                && Objects.equals(returnDeadlineDate, that.returnDeadlineDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, user, rentalDate, returnDeadlineDate);
    }
}
