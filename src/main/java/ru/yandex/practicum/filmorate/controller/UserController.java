package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.util.BadRequestException;
import ru.yandex.practicum.filmorate.util.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users", produces = "application/json")
public class UserController {

    private final UserStorage userStorage;

    @GetMapping
    public List<User> getUsers() {
        log.info("GET-запрос на получение всех пользователей.");
        return userStorage.getUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        log.info("GET-запрос на получение пользователя по id.");
        return userStorage.getUserById(Integer.parseInt(id));
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("POST-запрос на создание пользователя.");
        return userStorage.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("PUT-запрос на изменение существующего пользователя.");
        return userStorage.updateUser(user);
    }
}
