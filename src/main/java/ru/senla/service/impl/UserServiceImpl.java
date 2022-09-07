package ru.senla.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.exception.UserServiceOperationException;
import ru.senla.model.User;
import ru.senla.repository.UserRepository;
import ru.senla.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final static Logger LOGGER = Logger.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            this.userRepository.saveUser(user);
        } catch (Exception e) {
            String errorMessage = "Ошибка при сохранении пользователя.";
            LOGGER.error(String.format("%s %s", errorMessage, e.getMessage()), e);
            LOGGER.debug(String.format("Пользователь: %s.", user.toString()));
            throw new UserServiceOperationException(errorMessage, e);
        }
    }

    @Transactional
    @Override
    public void updateUserPersonalInfo(String login, String email, String name, String address, String phoneNumber) {
        try {
            User user = getUserByLogin(login);
            if (email != null)
                user.setEmail(email);
            if (name != null)
                user.setName(name);
            if (address != null)
                user.setAddress(address);
            if (phoneNumber != null)
                user.setPhoneNumber(phoneNumber);
        } catch (Exception e) {
            String errorMessage = "Ошибка при обновлении личной информации пользователя.";
            LOGGER.error(String.format("%s %s", errorMessage, e.getMessage()), e);
            LOGGER.debug(String.format("Логин пользователя: %s, новый email: %s, новое имя: %s, новый адрес: %s, новый номер телефона: %s.",
                                        login, email, name, address, phoneNumber));
            throw new UserServiceOperationException(errorMessage, e);
        }
    }

    @Override
    public User getUserById(Long id) {
        try {
            return this.userRepository.getUserById(id);
        } catch (Exception e) {
            String errorMessage = "Ошибка при получения пользователя по id.";
            LOGGER.error(String.format("%s %s", errorMessage, e.getMessage()), e);
            LOGGER.debug(String.format("Id пользователя: %d.", id));
            throw new UserServiceOperationException(errorMessage, e);
        }
    }

    @Override
    public User getUserByLogin(String login) {
        try {
            return this.userRepository.getUserByLogin(login);
        } catch (Exception e) {
            String errorMessage = "Ошибка при получения пользователя по логину.";
            LOGGER.error(String.format("%s %s", errorMessage, e.getMessage()), e);
            LOGGER.debug(String.format("Логин пользователя: %s.", login));
            throw new UserServiceOperationException(errorMessage, e);
        }
    }

    @Override
    public User getUserByLoginAndPassword(String login, String password) {
        User user = getUserByLogin(login);
        if (passwordEncoder.matches(password, user.getPassword()))
            return user;
        else {
            String errorMessage = "Ошибка при получения пользователя по логину и паролю.";
            LOGGER.error(errorMessage);
            LOGGER.debug(String.format("Логин пользователя: %s.", login));
            throw new UserServiceOperationException(errorMessage);
        }
    }
}
