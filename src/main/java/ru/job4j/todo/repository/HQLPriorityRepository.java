package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Repository
public class HQLPriorityRepository implements PriorityRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(HQLPriorityRepository.class.getName());

    private final CrudRepository crudRepository;

    @Override
    public List<Priority> findAll() {
        List<Priority> rsl = new ArrayList<>();
        try {
            rsl = crudRepository.query("from Priority", Priority.class);
        } catch (Exception e) {
            LOGGER.error("Приоритеты не найдены", e);
        }
        return rsl;
    }
}
