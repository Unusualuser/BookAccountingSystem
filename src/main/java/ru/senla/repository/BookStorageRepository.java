package ru.senla.repository;

public interface BookStorageRepository {
    void addQuantityByBookId(Long id, Long additionalQuantity);

    void reduceQuantityByBookId(Long id, Long quantityToBeReduce);

    void incrementQuantityByBookId(Long id);

    void decrementQuantityByBookId(Long id);

    Long getQuantityByBookId(Long id);

    void deleteBookStoragesByBookId(Long bookId);
}
