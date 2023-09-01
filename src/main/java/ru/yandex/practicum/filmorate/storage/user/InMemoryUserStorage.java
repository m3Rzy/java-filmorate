package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 0;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void add(User user) {
        user.setFriends(new HashSet<>());
        user.setId(getUserId());
        users.put(user.getId(), user);
    }

    @Override
    public Optional<User> update(User user) {
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        return Optional.of(user);
    }

    @Override
    public List<User> findFriends(Integer id) {
        return findAll().stream()
                .filter(user -> user.getFriends().contains(id))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> addFriend(Integer userId, Integer friendId) {
        findById(userId).get().getFriends().add(friendId);
        findById(friendId).get().getFriends().add(userId);
        return findById(userId);
    }

    @Override
    public Optional<User> removeFriend(Integer userId, Integer friendId) {
        findById(userId).get().getFriends().remove(friendId);
        findById(friendId).get().getFriends().remove(userId);
        return findById(userId);
    }

    @Override
    public List<User> findCommonFriends(Integer id, Integer otherId) {
        List<User> friends = new ArrayList<>();
        for (Integer i : findById(id).get().getFriends()) {
            if (findById(otherId).get().getFriends().contains(i)) {
                friends.add(findById(i).get());
            }
        }
        return friends;
    }

    private int getUserId() {
        return ++id;
    }
}
