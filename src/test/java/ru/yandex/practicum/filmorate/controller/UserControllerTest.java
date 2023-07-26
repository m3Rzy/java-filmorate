package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;


@DisplayName("Контроллер пользователя")
class UserControllerTest {
    private UserController userController;
    private UserStorage userStorage;
    private User user;

    @BeforeEach
    protected void start() {
        userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        userController = new UserController(userStorage, userService);
        user = User.builder()
                .email("test@ya.ru")
                .login("admin")
                .name("Admin")
                .birthday(LocalDate.of(2002, 4, 3))
                .build();
    }

    @DisplayName("Добавление пользователя с корректными значениями")
    @Test
    void addNewUser_correct_isOkRequest() {
        userController.addUser(user);
        Assertions.assertEquals(user, userStorage.getUserById(1));
    }

    @DisplayName("Добавление пользователя с пустым именем")
    @Test
    void addNewUser_nameIsBlank_isOkRequest() {
        user.setName("");
        userController.addUser(user);
        Assertions.assertEquals("admin", userController.getUsers().get(0).getName());
    }

    @DisplayName("Добавление пользователя с некорректным значением почты")
    @Test
    void addNewUser_incorrectEmail_isBadRequest() {
    }

    @DisplayName("Добавление пользователя с пустым логином")
    @Test
    void addNewUser_incorrectLogin_isBadRequest() {
    }

    @DisplayName("Добавление пользователя с др в будущем")
    @Test
    void addNewUser_incorrectDateInFuture_isBadRequest() {
    }
}