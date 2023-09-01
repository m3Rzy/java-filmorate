package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Set<Integer> findAll(int filmId) {
        Set<Integer> likes = new HashSet<>();
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet("SELECT like_id," +
                " film_id, user_id FROM likes");
        while (likeRows.next()) {
            if (likeRows.getInt("film_id") == filmId) {
                likes.add(likeRows.getInt("like_id"));
            }
        }
        return likes;
    }
}
