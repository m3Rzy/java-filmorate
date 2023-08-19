package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.util.NotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Primary
@Component
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorage mpaDbStorage;
    private final LikeStorage likeDbStorage;
    private final GenreDbStorage genreDbStorage;

    @Override
    public List<Film> findAll() {
        List<Film> films = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT film_id, " +
                "name, description, release_date," +
                " duration, rating_mpa_id FROM films");
        while (filmRows.next()) {
            Film film = Film.builder()
                    .id(filmRows.getInt("film_id"))
                    .name(filmRows.getString("name"))
                    .description(filmRows.getString("description"))
                    .releaseDate(Objects.requireNonNull(filmRows
                            .getDate("release_date")).toLocalDate())
                    .duration(filmRows.getInt("duration"))
                    .mpa(mpaDbStorage.findById(filmRows.getInt("rating_mpa_id")).get())
                    .build();
            film.setGenres(genreDbStorage.findForFilm(film.getId()));
            film.setLikes(likeDbStorage.findAll(film.getId()));
            films.add(film);
        }
        return films;
    }

    @Override
    public Film findById(int id) {
        String query = "SELECT film_id, name, description, release_date, duration, rating_mpa_id " +
                "FROM films WHERE film_id=?";
        return jdbcTemplate.queryForObject(query, this::mapRowToFilm, id);
    }

    @Override
    public void add(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(toMap(film)).intValue());
        mpaDbStorage.add(film);
        genreDbStorage.addGenreToFilm(film);
        genreDbStorage.addForFilm(film);
    }

    @Override
    public Optional<Film> update(Film film) {
        String query = "UPDATE films SET " +
                "name=?, description=?, release_date=?, " +
                "duration=?, rating_mpa_id=? WHERE film_id=?";
        int rowsCount = jdbcTemplate.update(query,
                film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        mpaDbStorage.add(film);
        genreDbStorage.update(film);
        genreDbStorage.addGenreToFilm(film);
        film.setGenres(genreDbStorage.findForFilm(film.getId()));
        if (rowsCount > 0) {
            return Optional.of(film);
        }
        throw new NotFoundException("Данный фильм не найден.");
    }

    @Override
    public Optional<Film> like(int filmId, int userId) {
        Film film = findById(filmId);
        String query = "INSERT INTO likes (film_id, user_id) VALUES(?, ?)";
        jdbcTemplate.update(query, filmId, userId);
        return Optional.of(film);
    }

    @Override
    public Optional<Film> removeLike(int filmId, int userId) {
        Film film = findById(filmId);
        String query = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(query, filmId, userId);
        return Optional.of(film);
    }

    @Override
    public List<Film> getRating(int limit) {
        String query = "SELECT films.*, COUNT(l.film_id) as count FROM films\n" +
                "LEFT JOIN likes l ON films.film_id=l.film_id\n" +
                "GROUP BY films.film_id\n" +
                "ORDER BY count DESC\n" +
                "LIMIT ?";
        return jdbcTemplate.query(query, this::mapRowToFilm, limit);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(mpaDbStorage.findById(resultSet.getInt("rating_mpa_id")).get())
                .build();
        film.setLikes(likeDbStorage.findAll(film.getId()));
        film.setGenres(genreDbStorage.findForFilm(film.getId()));
        return film;
    }

    private Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rating_mpa_id", film.getMpa().getId());
        return values;
    }
}
