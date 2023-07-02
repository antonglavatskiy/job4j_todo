package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HQLUserRepository implements UserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(HQLUserRepository.class.getName());

    private final CrudRepository crudRepository;

    @Override
    public Optional<User> save(User user) {
        Optional<User> rsl = Optional.empty();
        try {
            crudRepository.run(session -> session.save(user));
            rsl = Optional.of(user);
        } catch (Exception e) {
            LOGGER.error("Пользователь не сохранен", e);
        }
        return rsl;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Optional<User> rsl = Optional.empty();
        try {
            rsl = crudRepository.optional("from User u WHERE u.login = :fLogin AND u.password = :fPassword",
                    User.class, Map.of("fLogin", login, "fPassword", password));
        } catch (Exception e) {
            LOGGER.error("Пользователь не найден", e);
        }
        return rsl;
    }

    @Override
    public boolean deleteById(int id) {
        boolean rsl = false;
        try {
            rsl = crudRepository.run("DELETE User u WHERE u.id = :fId",
                    Map.of("fId", id));
        } catch (Exception e) {
            LOGGER.error("Пользователь не удален", e);
        }
        return rsl;
    }

    @Override
    public List<User> findAll() {
        List<User> rsl = new ArrayList<>();
        try {
            rsl = crudRepository.query("from User", User.class);
        } catch (Exception e) {
            LOGGER.error("Пользователи не найдены", e);
        }
        return rsl;
    }
}
