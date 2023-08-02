package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    //    получение всех пользователей
    List<User> findUsers();

    //    получение пользователя по id
    User findUserById(int id);

    //    создание нового пользователя
    void addUserStorage(User user);

    //    изменение существующего пользователя
    User updateUserStorage(User user);

    //    получение списка друзей у пользователя по id
    List<User> findFriendsByUserIdStorage(Integer id);

    //    добавление в список друзей
    User addFriendStorage(Integer userId, Integer friendId);

    //    удаление друга из списка
    User removeFriendStorage(Integer userId, Integer friendId);

    //    обновление списка друзей
    List<User> findCommonFriendsStorage(Integer id, Integer friendId);
}
