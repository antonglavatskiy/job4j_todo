package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@AllArgsConstructor
@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    private final PriorityService priorityService;

    private final CategoryService categoryService;

    private void setTime(Task task, User user) {
        String zoneId = user.getTimezone();
        ZoneId defTz = TimeZone.getDefault().toZoneId();
        LocalDateTime created = ZonedDateTime.of(task.getCreated(), defTz)
                .withZoneSameInstant(ZoneId.of(zoneId))
                .toLocalDateTime();
        task.setCreated(created);
    }

    @GetMapping
    public String getAll(Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        List<Task> tasks = taskService.findAll();
        tasks.forEach(task -> setTime(task, user));
        model.addAttribute("tasks", tasks);
        model.addAttribute("zone", ZoneId.of(user.getTimezone()));
        return "tasks/list";
    }

    @GetMapping("/done")
    public String getAllDone(Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        List<Task> tasks = taskService.findAllDone(true);
        tasks.forEach(task -> setTime(task, user));
        model.addAttribute("tasks", tasks);
        model.addAttribute("zone", ZoneId.of(user.getTimezone()));
        return "tasks/list";
    }

    @GetMapping("/new")
    public String getAllNew(Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        List<Task> tasks = taskService.findAllDone(false);
        tasks.forEach(task -> setTime(task, user));
        model.addAttribute("tasks", tasks);
        model.addAttribute("zone", ZoneId.of(user.getTimezone()));
        return "tasks/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, @RequestParam List<Integer> categoryList, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        task.setUser(user);
        task.setCategories(categoryService.findByIds(categoryList));
        taskService.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id, HttpServletRequest request) {
        Optional<Task> task = taskService.findById(id);
        if (task.isEmpty()) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("zone", ZoneId.of(user.getTimezone()));
        model.addAttribute("task", task.get());
        return "tasks/one";
    }

    @GetMapping("/check/{id}")
    public String getCheckDone(Model model, @PathVariable int id) {
        boolean isUpdate = taskService.checkDone(id);
        if (!isUpdate) {
            model.addAttribute("message", "Задача с указанным идентификатором не обновлена");
            return "errors/404";
        }
        return "redirect:/tasks/{id}";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        boolean isDeleted = taskService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/update/{id}")
    public String getUpdatePage(Model model, @PathVariable int id) {
        Optional<Task> task = taskService.findById(id);
        if (task.isEmpty()) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("task", task.get());
        return "tasks/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, Model model, @RequestParam List<Integer> categoryList, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        task.setUser(user);
        task.setCategories(categoryService.findByIds(categoryList));
        boolean isUpdated = taskService.update(task);
        if (!isUpdated) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/tasks";
    }
}
