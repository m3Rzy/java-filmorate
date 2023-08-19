package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    //    получение всех пользователей
    List<User> findAll();

    //    получение пользователя по id
    Optional<User> findById(int id);

    //    создание нового пользователя
    void add(User user);

    //    изменение существующего пользователя
    Optional<User> update(User user);

    //    получение списка друзей у пользователя по id
    List<User> findFriends(Integer id);

    //    добавление в список друзей
    Optional<User> addFriend(Integer userId, Integer friendId);

    //    удаление друга из списка
    Optional<User> removeFriend(Integer userId, Integer friendId);

    //    обновление списка друзей
    List<User> findCommonFriends(Integer id, Integer friendId);
}
