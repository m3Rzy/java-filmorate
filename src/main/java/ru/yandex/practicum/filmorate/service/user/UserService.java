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

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private void userValidationService(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public User addUser(User user) {
        userValidationService(user);
        userStorage.addUserStorage(user);
        log.info("Пользователь {} успешно создан.", user);
        return user;
    }

    public User updateUser(User user) {
        if (userStorage.findUserById(user.getId()) == null) {
            throw new NotFoundException("PUT - Пользователь с id " + user.getId() + " не существует.");
        }
        userValidationService(user);
        userStorage.updateUserStorage(user);
        log.info("Пользователь {} успешно изменён.", user);
        return user;
    }

    public User getUserById(int id) {
        if (userStorage.findUserById(id) == null) {
            throw new NotFoundException("GET - Пользователь с id " + id + " не был найден.");
        }
        userStorage.findUserById(id);
        log.info("Пользователь {} был успешно найден с помощью id.", userStorage.findUserById(id));
        return userStorage.findUserById(id);
    }

    public List<User> getUsers() {
        log.info("Количество пользователей в списке: " + userStorage.findUsers().size());
        return userStorage.findUsers();
    }

    public User addFriend(int userId, int friendId) {
        if (userId <= 0 || friendId <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id & friendId не могут быть отрицательными.");
        }
        userStorage.addFriendStorage(userId, friendId);
        log.info("Пользователь {} добавил в друзья {}", userId, friendId);
        return userStorage.findUserById(userId);
    }

    public User removeFriend(int userId, int friendId) {
        userStorage.removeFriendStorage(userId, friendId);
        log.info("Пользователь {} удалил из друзей {}", userId, friendId);
        return userStorage.findUserById(userId);
    }

    public List<User> getFriendsByUserId(Integer id) {
        log.info("Количество друзей: " + userStorage.findFriendsByUserIdStorage(id).size());
        return userStorage.findFriendsByUserIdStorage(id);
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        log.info("Количество общих друзей: " + userStorage.findCommonFriendsStorage(userId, otherId));
        return userStorage.findCommonFriendsStorage(userId, otherId);
    }
}
