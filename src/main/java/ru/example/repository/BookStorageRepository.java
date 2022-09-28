package ru.example.repository;

import ru.example.model.BookStorage;

public interface BookStorageRepository {
    void addQuantityByBookId(Long id, Long additionalQuantity);

    void reduceQuantityByBookId(Long id, Long quantityToBeReduce);

    void decrementQuantityByBookId(Long id);

    Long getQuantityByBookId(Long id);

    BookStorage getBookStorageByBookId(Long id);

    void deleteBookStorageByBookId(Long bookId);
}
