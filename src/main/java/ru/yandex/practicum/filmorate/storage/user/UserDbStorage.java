package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.NotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM users");
        while (rowSet.next()) {
            User user = User.builder()
                    .id(rowSet.getInt("user_id"))
                    .name(rowSet.getString("name"))
                    .login(rowSet.getString("login"))
                    .email(Objects.requireNonNull(rowSet.getString("email")))
                    .birthday(Objects.requireNonNull(rowSet.getDate("birthday")).toLocalDate())
                    .build();
            users.add(user);
        }
        log.info("UserDbStorage, запрос на получение списка всех пользователей прошёл успешно.");
        return users;
    }

    @Override
    public Optional<User> findById(int id) {
        String query = "SELECT user_id, email, login, name, birthday FROM users WHERE user_id=?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, this::mapRowToUser, id));
    }

    @Override
    public void add(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(toMap(user)).intValue());
        log.info("UserDbStorage, запрос на добавление пользователя в БД прошёл успешно");
    }

    @Override
    public Optional<User> update(User user) {
        String query = "UPDATE users SET " +
                "email=?, login=?, name=?, birthday=? WHERE user_id=?";
        int rowsCount = jdbcTemplate.update(query, user.getEmail(),
                user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        if (rowsCount > 0) {
            log.info("UserDbStorage, запрос на изменение пользователя прошёл успешно.");
            return Optional.of(user);
        }
        throw new NotFoundException("Данный пользовать не найден.");
    }

    @Override
    public List<User> findFriends(Integer id) {
        String query = "SELECT user_id, email, login, name, birthday FROM users WHERE" +
                " user_id IN (SELECT friend_id FROM friends WHERE user_id=?)";
        return new ArrayList<>(jdbcTemplate.query(query, this::mapRowToUser, id));
    }

    @Override
    public Optional<User> addFriend(Integer userId, Integer friendId) {
        String query = "INSERT INTO friends (user_id, friend_id) VALUES(?, ?)";
        jdbcTemplate.update(query, userId, friendId);
        return findById(userId);
    }

    @Override
    public Optional<User> removeFriend(Integer userId, Integer friendId) {
        String query = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(query, userId, friendId);
        return findById(userId);
    }

    @Override
    public List<User> findCommonFriends(Integer id, Integer friendId) {
        String query = "SELECT user_id, email, login, name, birthday FROM users WHERE user_id IN(" +
                "SELECT friend_id FROM friends WHERE user_id = ?) " +
                "AND user_id IN(SELECT friend_id FROM friends WHERE user_id = ?)";
        return new ArrayList<>(jdbcTemplate.query(query, this::mapRowToUser, id, friendId));
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = User.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
        user.setFriends(findFriends(user.getId()).stream()
                .map(User::getId).collect(Collectors.toSet()));
        return user;
    }

    private Map<String, Object> toMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("birthday", user.getBirthday());
        return values;
    }
}
