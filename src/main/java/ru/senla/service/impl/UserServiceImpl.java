package ru.senla.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.exception.InternalException;
import ru.senla.exception.UserAlreadyExistException;
import ru.senla.exception.UserNotFoundException;
import ru.senla.model.User;
import ru.senla.repository.UserRepository;
import ru.senla.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final static Logger LOGGER = Logger.getLogger(UserServiceImpl.class);
    private UserRepository userRepository;
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void registerUser(User user) {
        if (userRepository.containsByLogin(user.getLogin())) {
            throw new UserAlreadyExistException(user.getLogin());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveUser(user);
    }

    @Transactional
    @Override
    public void updateUserPersonalInfo(String login, String email, String name, String address, String phoneNumber) {
        try {
            User user = getUserByLogin(login);
            if (email != null) {
                user.setEmail(email);
            }
            if (name != null) {
                user.setName(name);
            }
            if (address != null) {
                user.setAddress(address);
            }
            if (phoneNumber != null) {
                user.setPhoneNumber(phoneNumber);
            }
        } catch (UserNotFoundException e) {
            LOGGER.debug(String.format("%s %s", "Ошибка при обновлении информации о пользователе.", e.getMessage()), e);
            throw new InternalException();
        }
    }

    @Transactional
    @Override
    public User getUserById(Long id) {
        try {
            User user = userRepository.getUserById(id);
            return new User(user.getName(),
                            user.getAddress(),
                            user.getPhoneNumber(),
                            user.getId(),
                            user.getLogin(),
                            user.getPassword(),
                            user.getRole(),
                            user.getEmail());
        } catch (ObjectNotFoundException e) {
            String errorMessage = String.format("Пользователь с id %d не найден.", id);
            LOGGER.debug(String.format("%s %s", "Ошибка при получения пользователя по id.", e.getMessage()), e);
            LOGGER.error(errorMessage);
            throw new UserNotFoundException(errorMessage);
        }
    }

    @Override
    public User getUserByLogin(String login) {
        try {
            return userRepository.getUserByLogin(login);
        } catch (ObjectNotFoundException e) {
            String errorMessage = String.format("Пользователь с логином %s не найден.", login);
            LOGGER.debug(String.format("%s %s", "Ошибка при получения пользователя по логину.", e.getMessage()), e);
            LOGGER.error(errorMessage);
            throw new UserNotFoundException(errorMessage);
        }
    }

    @Override
    public User getUserByLoginAndPassword(String login, String password) {
        User user = getUserByLogin(login);
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            LOGGER.debug("Ошибка при получения пользователя по логину и паролю.");
            throw new BadCredentialsException("Пользователь с указанными данными не найден.");
        }
    }
}
