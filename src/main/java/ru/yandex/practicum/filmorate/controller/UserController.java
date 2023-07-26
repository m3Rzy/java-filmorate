package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
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
    private final UserService userService;

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

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable String id) {
        log.info("GET-запрос на получение списка друзей.");
        return userService.getFriendsByUserId(Integer.parseInt(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable String id, @PathVariable String otherId) {
        log.info("GET-запрос на получение общих друзей.");
        return userService.getCommonFriends(Integer.parseInt(id), Integer.parseInt(otherId));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriends(@PathVariable String id, @PathVariable String friendId) {
        log.info("PUT-запрос на добавление пользователя в список друзей.");
        return userService.addFriend(Integer.parseInt(id), Integer.parseInt(friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable String id, @PathVariable String friendId) {
        log.info("DELETE-запрос на удаление из списка друзей.");
        userService.removeFriend(Integer.parseInt(id), Integer.parseInt(friendId));
    }
}
