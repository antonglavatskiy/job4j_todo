package ru.job4j.todo.service;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task save(Task task);
    List<Task> findAll();
    List<Task> findAllDone(boolean isDone);
    Optional<Task> findById(int id);
    boolean checkDone(int id);
    boolean deleteById(int id);
    boolean update(Task task);
}
