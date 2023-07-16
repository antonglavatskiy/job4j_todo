package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.*;

@AllArgsConstructor
@Repository
public class HQLCategoryRepository implements CategoryRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(HQLCategoryRepository.class.getName());

    private final CrudRepository crudRepository;

    @Override
    public List<Category> findAll() {
        List<Category> rsl = new ArrayList<>();
        try {
            rsl = crudRepository.query("from Category", Category.class);
        } catch (Exception e) {
            LOGGER.error("Категории не найдены", e);
        }
        return rsl;
    }

    @Override
    public List<Category> findById(List<Integer> list) {
        List<Category> rsl = new ArrayList<>();
        try {
            rsl = crudRepository.query("from Category c WHERE c.id IN :fId",
                    Category.class, Map.of("fId", list));
        } catch (Exception e) {
            LOGGER.error("Категория не найдена", e);
        }
        return rsl;
    }

}
