package ru.example.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.exception.UserAlreadyExistException;
import ru.example.model.User;
import ru.example.repository.UserRepository;
import ru.example.service.UserService;

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

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
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
    }

    @Transactional
    @Override
    public User getUserById(Long id) {
        User user = userRepository.getUserById(id);

        return new User(user.getName(),
                        user.getAddress(),
                        user.getPhoneNumber(),
                        user.getId(),
                        user.getLogin(),
                        user.getPassword(),
                        user.getRole(),
                        user.getEmail());
    }

    @Override
    public User getUserByLogin(String login) {
        return userRepository.getUserByLogin(login);
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
