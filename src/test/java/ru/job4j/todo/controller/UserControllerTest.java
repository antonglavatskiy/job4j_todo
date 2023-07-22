package ru.job4j.todo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;


import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private HttpServletRequest request;

    private UserService userService;

    private UserController userController;

    @BeforeEach
    public void initServices() {
        request = new MockHttpServletRequest();
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenGetRegistrationPage() {
        var model = new ConcurrentModel();
        var view = userController.getRegistationPage(model);
        assertThat(view).isEqualTo("users/register");
    }

    @Test
    public void whenRegisterUserThenRedirectIndexPage() {
        var user = new User(1, "name", "login", "pass", "UTC");
        when(userService.save(any())).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.register(user, model);

        assertThat(view).isEqualTo("redirect:/index");
    }

    @Test
    public void whenGetLoginPage() {
        var view = userController.getLoginPage();
        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenLoginUserThenRedirectTasksPage() {
        var user = new User(1, "name", "login", "pass", "UTC");
        when(userService.findByLoginAndPassword(any(), any())).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.loginUser(user, model, request);

        assertThat(view).isEqualTo("redirect:/tasks");
    }

    @Test
    public void whenLoginUserThenRedirectMessagePage() {
        var user = new User(1, "name", "login", "pass", "UTC");
        var expectedException = new RuntimeException("Логин или пароль введены неверно");
        when(userService.findByLoginAndPassword(user.getLogin(), user.getPassword())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = userController.loginUser(user, model, request);
        var actualExceptionMessage = model.getAttribute("error");

        assertThat(view).isEqualTo("users/login");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenLogoutUserThenRedirectLoginPage() {
        var view = userController.logout(request.getSession());
        assertThat(view).isEqualTo("redirect:/users/login");
    }
}