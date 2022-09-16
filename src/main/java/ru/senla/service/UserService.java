package ru.senla.service;

import ru.senla.model.User;

public interface UserService {
    void registerUser(User user);

    void updateUserPersonalInfo(String login, String email, String name, String address, String phoneNumber);

    User getUserById(Long id);

    User getUserByLogin(String login);

    User getUserByLoginAndPassword(String login, String password);
}
