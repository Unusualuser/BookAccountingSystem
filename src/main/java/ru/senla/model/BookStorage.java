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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "book_storage")
public class BookStorage implements Serializable {
    @Id
    @SequenceGenerator(name = "book_storage_seq", sequenceName = "book_storage_book_storage_id_seq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_storage_seq")
    @Column(name = "book_storage_id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", unique = true)
    private Book book;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    public BookStorage() {
    }

    public BookStorage(Long id, Book book, Long quantity) {
        this.id = id;
        this.book = book;
        this.quantity = quantity;
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

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookStorage)) return false;
        BookStorage that = (BookStorage) o;
        if (o instanceof HibernateProxy) {
            that = (BookStorage) Hibernate.unproxy(o);
        }
        return Objects.equals(id, that.id)
                && Objects.equals(book, that.book)
                && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, quantity);
    }
}
