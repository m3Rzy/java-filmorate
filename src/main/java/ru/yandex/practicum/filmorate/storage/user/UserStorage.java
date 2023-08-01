package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    //    получение всех пользователей
    List<User> findUsers();

    //    получение пользователя по id
    User findUserById(int id);

    //    создание нового пользователя
    User addUser(User user);

    //    изменение существующего пользователя
    User updateUser(User user);

    //    получение списка друзей у пользователя по id
    List<User> findFriendsByUserId(Integer id);

    //    добавление в список друзей
    User addFriend(Integer userId, Integer friendId);

    //    удаление друга из списка
    User removeFriend(Integer userId, Integer friendId);

    //    обновление списка друзей
    List<User> findCommonFriends(Integer id, Integer friendId);
}
