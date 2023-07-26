package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserStorage userStorage;

    public User addFriend(int userId, int friendId) {
        userStorage.addFriend(userId, friendId);
        return userStorage.getUserById(userId);
    }

    public User removeFriend(int userId, int friendId) {
        userStorage.removeFriend(userId, friendId);
        return userStorage.getUserById(userId);
    }

    public List<User> getFriendsByUserId(Integer id) {
        return userStorage.getFriendsByUserId(id);
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        return userStorage.getCommonFriends(userId, otherId);
    }
}
