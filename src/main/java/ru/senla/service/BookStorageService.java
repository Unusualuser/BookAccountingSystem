package ru.senla.service;


public interface BookStorageService {
    void addQuantityByBookIdCloseRequestsIfExistsAndNotifyUsers(Long id, Long additionalQuantity);

    void decrementQuantityByBookId(Long id);

    void deleteBookStorageByBookId(Long bookId);

    void reduceQuantityByBookId(Long id, Long quantityToBeReduce);

    Long getQuantityByBookId(Long id);
}
