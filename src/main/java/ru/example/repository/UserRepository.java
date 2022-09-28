package ru.example.repository;

import ru.example.model.User;

public interface UserRepository {
    void saveUser(User user);

    User getUserById(Long id);

    User getUserByLogin(String login);

    boolean containsByLogin(String login);
}
