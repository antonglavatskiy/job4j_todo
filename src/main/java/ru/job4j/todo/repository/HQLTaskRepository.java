package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.time.LocalDateTime;
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
        boolean rsl = false;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            rsl = session.createQuery(
                            "UPDATE Task t SET t.description = :fDescription,"
                                    + " t.created = :fCreated, t.done = :fDone"
                                    + " WHERE t.id = :fId")
                    .setParameter("fDescription", task.getDescription())
                    .setParameter("fCreated", LocalDateTime.now())
                    .setParameter("fDone", false)
                    .setParameter("fId", task.getId())
                    .executeUpdate() > 0;
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Задача не обновлена", e);
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
                            "DELETE Task t WHERE t.id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate() > 0;
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Задача не удалена", e);
        }
        return rsl;
    }

    @Override
    public Optional<Task> findById(int id) {
        Transaction transaction = null;
        Optional<Task> rsl = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<Task> query = session.createQuery(
                            "from Task t WHERE t.id = :fId", Task.class)
                    .setParameter("fId", id);
            rsl = query.uniqueResultOptional();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Задача не найдена", e);
        }
        return rsl;
    }

    @Override
    public boolean checkDone(int id) {
        boolean rsl = false;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            rsl = session.createQuery(
                    "UPDATE Task t SET t.done = :fDone WHERE t.id = :fId")
                    .setParameter("fDone", true)
                    .setParameter("fId", id)
                    .executeUpdate() > 0;
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Статус не обновлен", e);
        }
        return rsl;
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
        Transaction transaction = null;
        List<Task> rsl = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<Task> query = session.createQuery(
                    "from Task t WHERE t.done = :fDone", Task.class)
                    .setParameter("fDone", true);
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
    public List<Task> findAllNew() {
        Transaction transaction = null;
        List<Task> rsl = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<Task> query = session.createQuery(
                    "from Task t WHERE t.created >= :fCreate", Task.class)
                    .setParameter("fCreate", LocalDateTime.now().minusHours(12));
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
}
