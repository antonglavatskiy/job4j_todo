package ru.job4j.todo.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.model.Task;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HQLTaskRepositoryTest {

    private static HQLTaskRepository taskRepository;

    @BeforeAll
    public static void init() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        SessionFactory sessionFactory = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory();

        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        taskRepository = new HQLTaskRepository(crudRepository);
    }

    @AfterEach
    public void clearTasks() {
        List<Task> tasks = taskRepository.findAll();
        for (Task task : tasks) {
            taskRepository.deleteById(task.getId());
        }
    }

    @Test
    public void whenSaveTasksThenGetListAll() {
        LocalDateTime creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        Task task1 = taskRepository.save(new Task(0, "task1", "description1", creationDate.minusHours(2), false));
        Task task2 = taskRepository.save(new Task(0, "task2", "description2", creationDate.minusHours(1), false));
        Task task3 = taskRepository.save(new Task(0, "task3", "description3", creationDate, false));
        List<Task> result = taskRepository.findAll();
        assertThat(result).isEqualTo(List.of(task1, task2, task3));
    }

    @Test
    public void whenSaveTasksThenGetListNew() {
        LocalDateTime creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        Task task1 = taskRepository.save(new Task(0, "task1", "description1", creationDate.minusHours(2), true));
        Task task2 = taskRepository.save(new Task(0, "task2", "description2", creationDate.minusHours(1), false));
        Task task3 = taskRepository.save(new Task(0, "task3", "description3", creationDate, false));
        List<Task> resultNew = taskRepository.findAllDone(false);
        List<Task> resultAll = taskRepository.findAll();
        assertThat(resultNew).isEqualTo(List.of(task2, task3));
        assertThat(resultAll).isEqualTo(List.of(task1, task2, task3));
    }

    @Test
    public void whenSaveTaskThenCheckStatus() {
        LocalDateTime creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        Task task = taskRepository.save(new Task(0, "task", "description", creationDate, false));
        boolean result = taskRepository.checkDone(task.getId());
        assertThat(result).isTrue();
        assertThat(taskRepository.findAll().get(0).isDone()).isTrue();
    }

    @Test
    public void whenSaveTasksThenGetListDone() {
        LocalDateTime creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        Task task1 = taskRepository.save(new Task(0, "task1", "description1", creationDate, false));
        Task task2 = taskRepository.save(new Task(0, "task2", "description2", creationDate.plusHours(1), false));
        Task task3 = taskRepository.save(new Task(0, "task3", "description3", creationDate.plusHours(2), false));
        taskRepository.checkDone(task2.getId());
        List<Task> resultDone = taskRepository.findAllDone(true);
        List<Task> resultAll = taskRepository.findAll();
        assertThat(resultDone).isEqualTo(List.of(task2));
        assertThat(resultAll).isEqualTo(List.of(task1, task2, task3));
    }

    @Test
    public void whenSaveTaskThenFindById() {
        LocalDateTime creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        Task task = taskRepository.save(new Task(0, "task", "description", creationDate, false));
        Task savedTask = taskRepository.findById(task.getId()).get();
        assertThat(savedTask).usingRecursiveComparison().isEqualTo(task);
    }

    @Test
    public void whenDeleteTaskThenGetEmptyOptional() {
        LocalDateTime creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        Task task = taskRepository.save(new Task(0, "task", "description", creationDate, false));
        boolean isDeleted = taskRepository.deleteById(task.getId());
        Optional<Task> savedTask = taskRepository.findById(task.getId());
        assertThat(isDeleted).isTrue();
        assertThat(savedTask).isEqualTo(empty());
    }

    @Test
    public void whenSaveTaskThenGetUpdated() {
        Task task = taskRepository.save(new Task(0, "task", "description", LocalDateTime.now().minusHours(2), true));
        Task updatedTask = new Task(task.getId(), "new task", "new description", LocalDateTime.now(), false);
        boolean isUpdated = taskRepository.update(updatedTask);
        Task savedTask = taskRepository.findById(updatedTask.getId()).get();
        assertThat(isUpdated).isTrue();
        assertThat(savedTask.getDescription()).isEqualTo(updatedTask.getDescription());
        assertThat(savedTask.isDone()).isEqualTo(updatedTask.isDone());
    }
}