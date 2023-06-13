package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class HQLTaskRepository implements TaskRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(HQLTaskRepository.class.getName());

    private final SessionFactory sessionFactory;

    public HQLTaskRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Task save(Task task) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(task);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Задача не сохранена", e);
        }
        return task;
    }

    @Override
    public boolean update(Task task) {
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    @Override
    public Optional<Task> findById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Task> findAll() {
        Transaction transaction = null;
        List<Task> rsl = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<Task> query = session.createQuery(
                    "from Task", Task.class);
            rsl = query.list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Задачи не найдены", e);
        }
        return rsl;
    }

    @Override
    public List<Task> findAllDone() {
        return null;
    }

    @Override
    public List<Task> findAllNew() {
        return null;
    }
}
