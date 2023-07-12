package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HQLTaskRepository implements TaskRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(HQLTaskRepository.class.getName());

    private final CrudRepository crudRepository;

    @Override
    public Task save(Task task) {
        try {
            crudRepository.run(session -> session.save(task));
        } catch (Exception e) {
            LOGGER.error("Задача не сохранена", e);
        }
        return task;
    }

    @Override
    public boolean update(Task task) {
        boolean rsl = false;
        try {
            crudRepository.run(session -> session.update(task));
            rsl = true;
        } catch (Exception e) {
            LOGGER.error("Задача не обновлена", e);
        }
        return rsl;
    }

    @Override
    public boolean deleteById(int id) {
        boolean rsl = false;
        try {
            rsl = crudRepository.run("DELETE Task t WHERE t.id = :fId", Map.of("fId", id));
        } catch (Exception e) {
            LOGGER.error("Задача не удалена", e);
        }
        return rsl;
    }

    @Override
    public Optional<Task> findById(int id) {
        Optional<Task> rsl = Optional.empty();
        try {
            rsl = crudRepository.optional("from Task t JOIN FETCH t.priority WHERE t.id = :fId",
                    Task.class, Map.of("fId", id));
        } catch (Exception e) {
            LOGGER.error("Задача не найдена", e);
        }
        return rsl;
    }

    @Override
    public boolean checkDone(int id) {
        boolean rsl = false;
        try {
            rsl = crudRepository.run("UPDATE Task t SET t.done = true WHERE t.id = :fId",
                    Map.of("fId", id));
        } catch (Exception e) {
            LOGGER.error("Статус не обновлен", e);
        }
        return rsl;
    }

    @Override
    public List<Task> findAll() {
        List<Task> rsl = new ArrayList<>();
        try {
            rsl = crudRepository.query("from Task t JOIN FETCH t.priority ORDER BY t.created", Task.class);
        } catch (Exception e) {
            LOGGER.error("Задачи не найдены", e);
        }
        return rsl;
    }

    @Override
    public List<Task> findAllDone(boolean isDone) {
        List<Task> rsl = new ArrayList<>();
        try {
            rsl = crudRepository.query("from Task t JOIN FETCH t.priority WHERE t.done = :fDone ORDER BY t.created",
                    Task.class, Map.of("fDone", isDone));
        } catch (Exception e) {
            LOGGER.error("Задачи не найдены", e);
        }
        return rsl;
    }
}
