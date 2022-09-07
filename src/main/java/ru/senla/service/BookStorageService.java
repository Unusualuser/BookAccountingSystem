package ru.senla.service;


public interface BookStorageService {
    void addQuantityByBookIdCloseRequestsIfExistsAndNotifyUsers(Long id, Long additionalQuantity);

    void reduceQuantityByBookId(Long id, Long quantityToBeReduce);

    Long getQuantityByBookId(Long id);
}
