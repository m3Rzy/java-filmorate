package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.BadRequestException;
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
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else throw new BadRequestException("Пользователь не был создан.");
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
            throw new BadRequestException("Не удалось найти указанного пользователя." + user);
        }
        return user;
    }

    @Override
    public List<User> getFriendsByUserId(Integer id) {
        return getUsers().stream()
                .filter(user -> user.getFriends().contains(id))
                .collect(Collectors.toList());
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        getUserById(userId).getFriends().add(friendId);
        getUserById(friendId).getFriends().add(userId);
        return getUserById(userId);
    }

    @Override
    public User removeFriend(Integer userId, Integer friendId) {
        getUserById(userId).getFriends().remove(friendId);
        getUserById(friendId).getFriends().remove(userId);
        return getUserById(userId);
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        List<User> friends = new ArrayList<>();
        for (Integer i : getUserById(id).getFriends()) {
            if (getUserById(otherId).getFriends().contains(i)) {
                friends.add(getUserById(i));
            }
        }
        return friends;
    }
}
