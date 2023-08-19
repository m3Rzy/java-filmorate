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
        userDbStorage.add(user);
        userDbStorage.add(otherUser);
        userDbStorage.add(user2);
    }

    @DisplayName("Добавление нового пользователя.")
    @Test
    void shouldAddNewUser_isOkRequest() {
        addEntites();
        assertEquals("Елена", userDbStorage.findById(1).get().getName());
        assertFalse(userDbStorage.findAll().isEmpty());
    }

    @DisplayName("Изменение существующего пользователя.")
    @Test
    void shouldUpdateUser_isOkRequest() {
        addEntites();
        user.setName("Ольга");
        userDbStorage.update(user);
        assertEquals("Ольга", userDbStorage.findById(1).get().getName());
    }

    @DisplayName("Добавление пользователя в друзья.")
    @Test
    void shouldAddNewFriend_isOkRequest() {
        addEntites();
        userDbStorage.addFriend(user.getId(), otherUser.getId());
        assertFalse(userDbStorage.findById(1).get().getFriends().isEmpty());
        assertTrue(userDbStorage.findById(1).get().getFriends().contains(2));
        assertEquals("theft", userDbStorage.findFriends(1).get(0).getName());
    }

    @DisplayName("Удаление пользователя из списка друзей.")
    @Test
    void shouldDeleteFriend_isOkRequest() {
        addEntites();
        userDbStorage.addFriend(user.getId(), otherUser.getId());
        userDbStorage.removeFriend(user.getId(), otherUser.getId());
        assertTrue(userDbStorage.findById(1).get().getFriends().isEmpty());
    }

    @DisplayName("Наличие общих друзей.")
    @Test
    void shouldGetCommonFriends_isOkRequest() {
        addEntites();
        userDbStorage.addFriend(user.getId(), user2.getId());
        userDbStorage.addFriend(otherUser.getId(), user2.getId());
        System.out.println(userDbStorage.findCommonFriends(1, 2));
        assertEquals(3, userDbStorage.findFriends(2).get(0).getId());
        assertEquals(3, userDbStorage.findFriends(1).get(0).getId());
        assertSame(userDbStorage.findCommonFriends(user.getId(), otherUser.getId()).get(0).getId(), user2.getId());
    }
}
