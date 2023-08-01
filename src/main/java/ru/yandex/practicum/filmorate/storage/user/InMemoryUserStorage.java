package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.NotFoundException;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 0;

    @Override
    public List<User> findUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findUserById(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("Пользователь не был создан.");
        }
    }

    private int getUserId() {
        return ++id;
    }

    private void userValidation(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    @Override
    public User addUser(User user) {
        userValidation(user);
        user.setFriends(new HashSet<>());
        user.setId(getUserId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.get(user.getId()) != null) {
            userValidation(user);
            user.setFriends(new HashSet<>());
            users.put(user.getId(), user);
        } else {
            throw new NotFoundException("Не удалось найти указанного пользователя." + user);
        }
        return user;
    }

    @Override
    public List<User> findFriendsByUserId(Integer id) {
        return findUsers().stream()
                .filter(user -> user.getFriends().contains(id))
                .collect(Collectors.toList());
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        findUserById(userId).getFriends().add(friendId);
        findUserById(friendId).getFriends().add(userId);
        return findUserById(userId);
    }

    @Override
    public User removeFriend(Integer userId, Integer friendId) {
        findUserById(userId).getFriends().remove(friendId);
        findUserById(friendId).getFriends().remove(userId);
        return findUserById(userId);
    }

    @Override
    public List<User> findCommonFriends(Integer id, Integer otherId) {
        List<User> friends = new ArrayList<>();
        for (Integer i : findUserById(id).getFriends()) {
            if (findUserById(otherId).getFriends().contains(i)) {
                friends.add(findUserById(i));
            }
        }
        return friends;
    }
}
