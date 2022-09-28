package ru.example.repository.impl;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.example.exception.UserNotFoundException;
import ru.example.model.User;
import ru.example.repository.UserRepository;

import java.util.List;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {
    private final static Logger LOGGER = Logger.getLogger(UserRepositoryImpl.class);
    private final SessionFactory sessionFactory;

    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveUser(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    public User getUserById(Long id) {
        User user = sessionFactory.getCurrentSession().get(User.class, id);

        if (user == null) {
            String errorMessage = String.format("Пользователь с id %d не найден.", id);
            LOGGER.error(errorMessage);
            throw new UserNotFoundException(errorMessage);
        }

        return user;
    }

    @Override
    public User getUserByLogin(String login) {
         User user = getUserByLoginOrNullIfNotExists(login);

        if (user == null) {
            LOGGER.debug("Ошибка при получении пользователя по логину.");
            throw new UserNotFoundException(String.format("Пользователь c логином %s не найден.", login));
        }

        return user;
    }

    @Override
    public boolean containsByLogin(String login) {
        User user = getUserByLoginOrNullIfNotExists(login);

        return user != null;
    }

    private User getUserByLoginOrNullIfNotExists(String login) {
        List<User> userList = sessionFactory.getCurrentSession().createNativeQuery(
                 "SELECT user_id, name, address, phone_number, login, password, role, email " +
                    "FROM public.user " +
                    "WHERE login = :login", User.class)
                .setParameter("login", login)
                .list();

        if (userList.size() != 1) {
            return null;
        }

        return userList.get(0);
    }
}
