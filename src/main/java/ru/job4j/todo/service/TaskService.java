package ru.job4j.todo.service;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task save(Task task);
    List<Task> findAll();
    List<Task> findAllDone();
    List<Task> findAllNew();
    Optional<Task> findById(int id);
}
