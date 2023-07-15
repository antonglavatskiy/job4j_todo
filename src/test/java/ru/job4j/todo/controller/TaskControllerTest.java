package ru.job4j.todo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskControllerTest {

    private TaskService taskService;

    private PriorityService priorityService;

    private CategoryService categoryService;

    private TaskController taskController;

    private HttpServletRequest request;

    @BeforeEach
    public void initServices() {
        taskService = mock(TaskService.class);
        priorityService = mock(PriorityService.class);
        categoryService = mock(CategoryService.class);
        taskController = new TaskController(taskService, priorityService, categoryService);
        request = new MockHttpServletRequest();
    }

    @Test
    public void whenRequestTaskListPageThenGetPageWithTasks() {
        Task task1 = mock(Task.class);
        Task task2 = mock(Task.class);
        List<Task> expected = List.of(task1, task2);
        when(taskService.findAll()).thenReturn(expected);

        Model model = new ConcurrentModel();
        String view = taskController.getAll(model);
        Object actual = model.getAttribute("tasks");

        assertThat(view).isEqualTo("tasks/list");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenRequestTaskListPageThenGetPageWithTasksDone() {
        Task task = mock(Task.class);
        List<Task> expected = List.of(task);
        when(taskService.findAllDone(true)).thenReturn(expected);

        Model model = new ConcurrentModel();
        String view = taskController.getAllDone(model);
        Object actual = model.getAttribute("tasks");

        assertThat(view).isEqualTo("tasks/list");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenRequestTaskListPageThenGetPageWithTasksNew() {
        Task task = mock(Task.class);
        List<Task> expected = List.of(task);
        when(taskService.findAllDone(false)).thenReturn(expected);

        Model model = new ConcurrentModel();
        String view = taskController.getAllNew(model);
        Object actual = model.getAttribute("tasks");

        assertThat(view).isEqualTo("tasks/list");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenRequestCreatePageThenGetCreationPage() {
        Model model = new ConcurrentModel();
        String view = taskController.getCreationPage(model);

        assertThat(view).isEqualTo("tasks/create");
    }

    @Test
    public void whenRequestSaveTaskThenGetListTasks() {
        Task task = mock(Task.class);
        List<String> list = List.of("1");

        when(categoryService.findById(1)).thenReturn(Optional.of(new Category(1, "work")));
        when(taskService.save(any())).thenReturn(task);

        String view = taskController.create(task, list, request);
        assertThat(view).isEqualTo("redirect:/tasks");
    }

    @Test
    public void whenRequestTaskPageThenGetPageWithTask() {
        Task task = mock(Task.class);
        when(taskService.findById(any(Integer.class))).thenReturn(Optional.of(task));

        Model model = new ConcurrentModel();
        String view = taskController.getById(model, task.getId());
        Object expected = model.getAttribute("task");

        assertThat(view).isEqualTo("tasks/one");
        assertThat(expected).isEqualTo(task);
    }

    @Test
    public void whenRequestTaskPageThenGetErrorPage() {
        Task task = mock(Task.class);
        Exception expectedException = new RuntimeException("Задача с указанным идентификатором не найдена");
        when(taskService.findById(any(Integer.class))).thenReturn(Optional.empty());

        Model model = new ConcurrentModel();
        String view = taskController.getById(model, task.getId());
        Object actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenRequestChangeDoneTaskPageThenGetPageWithTask() {
        Task task = mock(Task.class);
        when(taskService.checkDone(any(Integer.class))).thenReturn(true);

        Model model = new ConcurrentModel();
        String view = taskController.getCheckDone(model, task.getId());

        assertThat(view).isEqualTo("redirect:/tasks/{id}");
    }

    @Test
    public void whenRequestChangeDoneTaskPageThenGetErrorPage() {
        Task task = mock(Task.class);
        Exception expectedException = new RuntimeException("Задача с указанным идентификатором не обновлена");
        when(taskService.checkDone(any(Integer.class))).thenReturn(false);

        Model model = new ConcurrentModel();
        String view = taskController.getCheckDone(model, task.getId());
        Object actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenRequestDeleteTaskThenGetListTasks() {
        Task task = mock(Task.class);
        when(taskService.deleteById(any(Integer.class))).thenReturn(true);

        Model model = new ConcurrentModel();
        String view = taskController.delete(model, task.getId());

        assertThat(view).isEqualTo("redirect:/tasks");
    }

    @Test
    public void whenRequestDeleteTaskThenGetErrorPage() {
        Task task = mock(Task.class);
        Exception expectedException = new RuntimeException("Задача с указанным идентификатором не найдена");
        when(taskService.deleteById(any(Integer.class))).thenReturn(false);

        Model model = new ConcurrentModel();
        String view = taskController.delete(model, task.getId());
        Object actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenRequestEditTaskThenGetUpdatePage() {
        Task task = mock(Task.class);
        when(taskService.findById(any(Integer.class))).thenReturn(Optional.of(task));

        Model model = new ConcurrentModel();
        String view = taskController.getUpdatePage(model, task.getId());
        model.addAttribute("task", task.getId());

        assertThat(view).isEqualTo("tasks/update");
    }

    @Test
    public void whenRequestEditTaskThenGetErrorPage() {
        Task task = mock(Task.class);
        Exception expectedException = new RuntimeException("Задача с указанным идентификатором не найдена");
        when(taskService.findById(any(Integer.class))).thenReturn(Optional.empty());

        Model model = new ConcurrentModel();
        String view = taskController.getUpdatePage(model, task.getId());
        Object actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenRequestUpdateTaskThenGetListTasks() {
        Task task = mock(Task.class);
        List<String> list = List.of("1");

        when(categoryService.findById(1)).thenReturn(Optional.of(new Category(1, "work")));
        when(taskService.update(task)).thenReturn(true);

        Model model = new ConcurrentModel();
        String view = taskController.update(task, model, list, request);

        assertThat(view).isEqualTo("redirect:/tasks");
    }

    @Test
    public void whenRequestUpdateTaskThenGetErrorPage() {
        Task task = mock(Task.class);
        List<String> list = List.of("1");
        Exception expectedException = new RuntimeException("Задача с указанным идентификатором не найдена");

        when(categoryService.findById(1)).thenReturn(Optional.of(new Category(1, "work")));
        when(taskService.update(task)).thenReturn(false);

        Model model = new ConcurrentModel();
        String view = taskController.update(task, model, list, request);
        Object actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }
}