package ru.job4j.todo.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HQLUserRepositoryTest {

    private static HQLUserRepository userRepository;

    @BeforeAll
    public static void init() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        SessionFactory sessionFactory = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory();

        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        userRepository = new HQLUserRepository(crudRepository);
    }

    @AfterEach
    public void clearUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            userRepository.deleteById(user.getId());
        }
    }

    @Test
    public void whenSaveUser() {
        Optional<User> user = userRepository.save(new User(0, "name",
                "login", "pass", "UTC"));
        assertThat(user).isNotEmpty();
    }

    @Test
    public void whenSaveUserThenFindByLoginAndPassword() {
        User user = userRepository.save(new User(0, "name",
                "login", "pass", "UTC")).get();
        User savedUser = userRepository.findByLoginAndPassword("login", "pass").get();
        assertThat(user).usingRecursiveComparison().isEqualTo(savedUser);
    }

    @Test
    public void whenSaveUsersThenGetListAll() {
        User user1 = userRepository.save(new User(0, "name1",
                "login1", "pass1", "UTC")).get();
        User user2 = userRepository.save(new User(0, "name2",
                "login2", "pass2", "UTC")).get();
        User user3 = userRepository.save(new User(0, "name3",
                "login3", "pass3", "UTC")).get();
        List<User> result = userRepository.findAll();
        assertThat(result).isEqualTo(List.of(user1, user2, user3));
    }

    @Test
    public void whenDeleteUserThenGetList() {
        User user1 = userRepository.save(new User(0, "name1",
                "login1", "pass1", "UTC")).get();
        User user2 = userRepository.save(new User(0, "name2",
                "login2", "pass2", "UTC")).get();
        User user3 = userRepository.save(new User(0, "name3",
                "login3", "pass3", "UTC")).get();
        assertThat(userRepository.deleteById(user2.getId())).isTrue();
        List<User> result = userRepository.findAll();
        assertThat(result).isEqualTo(List.of(user1, user3));
    }
}