package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

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
        return users.get(id);
    }

    private int getUserId() {
        return ++id;
    }

    @Override
    public void addUserStorage(User user) {
        user.setFriends(new HashSet<>());
        user.setId(getUserId());
        users.put(user.getId(), user);
    }

    @Override
    public User updateUserStorage(User user) {
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> findFriendsByUserIdStorage(Integer id) {
        return findUsers().stream()
                .filter(user -> user.getFriends().contains(id))
                .collect(Collectors.toList());
    }

    @Override
    public User addFriendStorage(Integer userId, Integer friendId) {
        findUserById(userId).getFriends().add(friendId);
        findUserById(friendId).getFriends().add(userId);
        return findUserById(userId);
    }

    @Override
    public User removeFriendStorage(Integer userId, Integer friendId) {
        findUserById(userId).getFriends().remove(friendId);
        findUserById(friendId).getFriends().remove(userId);
        return findUserById(userId);
    }

    @Override
    public List<User> findCommonFriendsStorage(Integer id, Integer otherId) {
        List<User> friends = new ArrayList<>();
        for (Integer i : findUserById(id).getFriends()) {
            if (findUserById(otherId).getFriends().contains(i)) {
                friends.add(findUserById(i));
            }
        }
        return friends;
    }
}
