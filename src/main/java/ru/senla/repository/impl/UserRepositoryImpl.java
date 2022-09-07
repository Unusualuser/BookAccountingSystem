package ru.senla.repository.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.model.User;
import ru.senla.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
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
        this.sessionFactory.getCurrentSession().save(user);
    }

    @Override
    public User getUserById(Long id) {
        return this.sessionFactory.getCurrentSession().load(User.class, id);
    }

    @Override
    public User getUserByLogin(String login) {
        List<User> userList = sessionFactory.getCurrentSession().createNativeQuery(
                 "SELECT user_id, name, address, phone_number, login, password, role, email " +
                    "FROM public.user " +
                    "WHERE login = :login", User.class)
                .setParameter("login", login)
                .list();

        if (userList.size() != 1)
            throw new EntityNotFoundException("Пользователь не найден.");

        return userList.get(0);
    }
}
