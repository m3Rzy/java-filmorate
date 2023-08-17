package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестовое хранилище UserDbStorage")
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserDbStorageTest {
    private final UserDbStorage userDbStorage;
    private User user;
    private User otherUser;
    private User user2;

    @BeforeEach
    void preparationEntities() {
        user = User.builder()
                .name("Елена")
                .email("elena@yandex.ru")
                .login("elena")
                .birthday(LocalDate.of(2002, 4, 3))
                .build();
        user.setFriends(new HashSet<>());

        otherUser = User.builder()
                .email("theft@ya.ru")
                .login("theft")
                .birthday(LocalDate.of(1998, 6, 6))
                .build();
        otherUser.setFriends(new HashSet<>());

        user2 = User.builder()
                .name("Саша")
                .email("sasha@yandex.ru")
                .login("sasha")
                .birthday(LocalDate.of(2002, 4, 3))
                .build();
        user2.setFriends(new HashSet<>());
    }

    void addEntites() {
        userDbStorage.addUserStorage(user);
        userDbStorage.addUserStorage(otherUser);
        userDbStorage.addUserStorage(user2);
    }

    @DisplayName("Добавление нового пользователя.")
    @Test
    void shouldAddNewUser_isOkRequest() {
        addEntites();
        assertEquals("Елена", userDbStorage.findUserById(1).getName());
        assertFalse(userDbStorage.findUsers().isEmpty());
    }

    @DisplayName("Изменение существующего пользователя.")
    @Test
    void shouldUpdateUser_isOkRequest() {
        addEntites();
        user.setName("Ольга");
        userDbStorage.updateUserStorage(user);
        assertEquals("Ольга", userDbStorage.findUserById(1).getName());
    }

    @DisplayName("Добавление пользователя в друзья.")
    @Test
    void shouldAddNewFriend_isOkRequest() {
        addEntites();
        userDbStorage.addFriendStorage(user.getId(), otherUser.getId());
        assertFalse(userDbStorage.findUserById(1).getFriends().isEmpty());
        assertTrue(userDbStorage.findUserById(1).getFriends().contains(2));
        assertEquals("theft", userDbStorage.findFriendsByUserIdStorage(1).get(0).getName());
    }

    @DisplayName("Удаление пользователя из списка друзей.")
    @Test
    void shouldDeleteFriend_isOkRequest() {
        addEntites();
        userDbStorage.addFriendStorage(user.getId(), otherUser.getId());
        userDbStorage.removeFriendStorage(user.getId(), otherUser.getId());
        assertTrue(userDbStorage.findUserById(1).getFriends().isEmpty());
    }

    @DisplayName("Наличие общих друзей.")
    @Test
    void shouldGetCommonFriends_isOkRequest() {
        addEntites();
        userDbStorage.addFriendStorage(user.getId(), user2.getId());
        userDbStorage.addFriendStorage(otherUser.getId(), user2.getId());
        System.out.println(userDbStorage.findCommonFriendsStorage(1, 2));
        assertEquals(3, userDbStorage.findFriendsByUserIdStorage(2).get(0).getId());
        assertEquals(3, userDbStorage.findFriendsByUserIdStorage(1).get(0).getId());
        assertSame(userDbStorage.findCommonFriendsStorage(user.getId(), otherUser.getId()).get(0).getId(), user2.getId());
    }
}
