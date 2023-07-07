package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.UserException;
import ru.yandex.practicum.filmorate.util.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/users", produces = "application/json")
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();
    private int userId = 0;

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    private int getUserId() {
        return ++userId;
    }

    private void userValidation(User user) throws ValidationException {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        userValidation(user);
        user.setId(getUserId());
        users.put(user.getId(), user);
        log.info("Запрос на создание пользователя пройден.");
        return user;
    }

    @PutMapping
    public User changeUser(@Valid @RequestBody User user) {
        if (users.get(user.getId()) != null) {
            userValidation(user);
            users.put(user.getId(), user);
            log.info("Запрос на изменение пользователя пройден.");
        } else {
            log.error("Запрос на изменение пользователя не пройден.");
            throw new UserException("Не удалось найти указанного пользователя.");
        }
        return user;
    }
}
