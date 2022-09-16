package ru.senla.repository;

import ru.senla.model.User;

public interface UserRepository {
    void saveUser(User user);

    User getUserById(Long id);

    User getUserByLogin(String login);

    boolean containsByLogin(String login);
}
