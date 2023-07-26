package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    //    получение всех пользователей
    List<User> getUsers();

    //    получение пользователя по id
    User getUserById(int id);

    //    создание нового пользователя
    User addUser(User user);

    //    изменение существующего пользователя
    User updateUser(User user);

    //    получение списка друзей у пользователя по id
    List<User> getFriendsByUserId(Integer id);

    //    добавление в список друзей
    User addFriend(Integer userId, Integer friendId);

    //    удаление друга из списка
    User removeFriend(Integer userId, Integer friendId);

    //    обновление списка друзей
    List<User> getCommonFriends(Integer id, Integer friendId);
}
