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
    public List<Film> findFilms() {
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
                    .mpa(mpaDbStorage.findMpaById(filmRows.getInt("rating_mpa_id")))
                    .build();
            film.setGenres(genreDbStorage.findGenresForCurrentFilm(film.getId()));
            film.setLikes(likeDbStorage.findLikesForCurrentFilm(film.getId()));

            films.add(film);
        }
        return films;
    }

    @Override
    public Film findFilmById(int id) {
        String query = "SELECT film_id, name, description, release_date, duration, rating_mpa_id " +
                "FROM films WHERE film_id=?";
        return jdbcTemplate.queryForObject(query, this::mapRowToFilm, id);
    }

    @Override
    public void addFilmStorage(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(toMap(film)).intValue());
        mpaDbStorage.addMpaToFilm(film);
        genreDbStorage.addGenreToFilm(film);
        genreDbStorage.addGenresForCurrentFilm(film);
    }

    @Override
    public Film updateFilmStorage(Film film) {
        String query = "UPDATE films SET " +
                "name=?, description=?, release_date=?, " +
                "duration=?, rating_mpa_id=? WHERE film_id=?";
        int rowsCount = jdbcTemplate.update(query,
                film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        mpaDbStorage.addMpaToFilm(film);
        genreDbStorage.updateGenresForCurrentFilm(film);
        genreDbStorage.addGenreToFilm(film);
        film.setGenres(genreDbStorage.findGenresForCurrentFilm(film.getId()));
        if (rowsCount > 0) {
            return film;
        }
        throw new NotFoundException("Данный фильм не найден.");
    }

    @Override
    public Film pressLike(int filmId, int userId) {
        Film film = findFilmById(filmId);
        String query = "INSERT INTO likes (film_id, user_id) VALUES(?, ?)";
        jdbcTemplate.update(query, filmId, userId);
        return film;
    }

    @Override
    public Film removeLike(int filmId, int userId) {
        Film film = findFilmById(filmId);
        String query = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(query, filmId, userId);
        return film;
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
                .mpa(mpaDbStorage.findMpaById(resultSet.getInt("rating_mpa_id")))
                .build();
        film.setLikes(likeDbStorage.findLikesForCurrentFilm(film.getId()));
        film.setGenres(genreDbStorage.findGenresForCurrentFilm(film.getId()));
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
