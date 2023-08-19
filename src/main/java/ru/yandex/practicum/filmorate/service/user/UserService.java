package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.util.NotFoundException;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Optional<User> addUser(User user) {
        userStorage.add(user);
        log.info("Пользователь {} успешно создан.", user);
        return Optional.ofNullable(user);
    }

    public Optional<User> updateUser(User user) {
        try {
            userStorage.update(user);
            log.info("Пользователь {} успешно изменён.", user);
            return Optional.ofNullable(user);
        } catch (RuntimeException e) {
            throw new NotFoundException("PUT - Пользователь с id " + user.getId() + " не существует.");
        }
    }

    public Optional<User> getUserById(int id) {
        try {
            userStorage.findById(id);
            log.info("Пользователь {} был успешно найден с помощью id.", userStorage.findById(id));
            return userStorage.findById(id);
        } catch (RuntimeException e) {
            throw new NotFoundException("Такой пользователь не найден.");
        }
    }

    public List<User> getUsers() {
        log.info("Количество пользователей в списке: " + userStorage.findAll().size());
        return userStorage.findAll();
    }

    public Optional<User> addFriend(int userId, int friendId) {
        if (userId <= 0 || friendId <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id & friendId не могут быть отрицательными.");
        }
        userStorage.addFriend(userId, friendId);
        log.info("Пользователь {} добавил в друзья {}", userId, friendId);
        return userStorage.findById(userId);
    }

    public Optional<User> removeFriend(int userId, int friendId) {
        userStorage.removeFriend(userId, friendId);
        log.info("Пользователь {} удалил из друзей {}", userId, friendId);
        return userStorage.findById(userId);
    }

    public List<User> getFriendsByUserId(Integer id) {
        log.info("Количество друзей: " + userStorage.findFriends(id).size());
        return userStorage.findFriends(id);
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        log.info("Количество общих друзей: " + userStorage.findCommonFriends(userId, otherId));
        return userStorage.findCommonFriends(userId, otherId);
    }
}
