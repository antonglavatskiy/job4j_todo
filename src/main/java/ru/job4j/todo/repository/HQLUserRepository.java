package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.todo.model.User;

import java.util.Optional;

public class HQLUserRepository implements UserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(HQLUserRepository.class.getName());

    private final SessionFactory sessionFactory;

    public HQLUserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<User> save(User user) {
        Transaction transaction = null;
        Optional<User> rsl = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            rsl = Optional.of(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Пользователь не сохранен", e);
        }
        return rsl;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Transaction transaction = null;
        Optional<User> rsl = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<User> query = session.createQuery(
                            "from User u WHERE u.login = :fLogin AND u.password = :fPassword", User.class)
                    .setParameter("fLogin", login)
                    .setParameter("fPassword", password);
            rsl = query.uniqueResultOptional();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Пользователь не найден", e);
        }
        return rsl;
    }
}
