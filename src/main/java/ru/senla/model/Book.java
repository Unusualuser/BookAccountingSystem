package ru.senla.model;

import jakarta.validation.constraints.Size;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "book", schema = "public")
public class Book implements Serializable {
    @Id
    @SequenceGenerator(name = "book_seq", sequenceName = "book_book_id_seq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq")
    @Column(name = "book_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @Size(min = 4, max = 30, message = "Не меньше 4 и не больше 30 символов")
    private String name;

    @Column(name = "publication_year", nullable = false)
    private Integer publicationYear;

    @Column(name = "author", nullable = false)
    @Size(min = 4, max = 100, message = "Не меньше 4 и не больше 100 символов")
    private String author;

    @Column(name = "description")
    @Size(max = 70, message = "Не больше 70 символов")
    private String description;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @PrePersist
    protected void onCreate() {
        this.amount = 0L;
    }

    public Book() {

    }

    public Book(Long id, String name, Integer publicationYear, String author, String description, Long amount) {
        this.id = id;
        this.name = name;
        this.publicationYear = publicationYear;
        this.author = author;
        this.description = description;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        if (o instanceof HibernateProxy) {
            book = (Book) Hibernate.unproxy(o);
        }
        return Objects.equals(id, book.id)
                && name.equals(book.name)
                && publicationYear.equals(book.publicationYear)
                && author.equals(book.author)
                && description.equals(book.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, publicationYear, author);
    }
}
