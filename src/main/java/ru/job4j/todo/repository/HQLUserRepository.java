package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
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
            transaction.commit();
            rsl = Optional.of(user);
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

    @Override
    public boolean deleteById(int id) {
        boolean rsl = false;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            rsl = session.createQuery(
                            "DELETE User u WHERE u.id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate() > 0;
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Пользователь не удален", e);
        }
        return rsl;
    }

    @Override
    public List<User> findAll() {
        Transaction transaction = null;
        List<User> rsl = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<User> query = session.createQuery(
                    "from User", User.class);
            rsl = query.list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Пользователи не найдены", e);
        }
        return rsl;
    }
}
