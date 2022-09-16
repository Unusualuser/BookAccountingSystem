package ru.senla.repository.impl;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.model.User;
import ru.senla.repository.UserRepository;

import java.util.List;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {
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
        return sessionFactory.getCurrentSession().load(User.class, id);
    }

    @Override
    public User getUserByLogin(String login) {
         User user = getUserByLoginOrNullIfNotExists(login);

        if (user == null) {
            throw new ObjectNotFoundException(User.class, String.format("Пользователь c логином %s не найден.", login));
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
