package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        List<Genre> genres = new ArrayList<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT genre_id," + "name FROM genre_type");
        while (genreRows.next()) {
            Genre genre = Genre.builder().id(genreRows.getInt("genre_id")).name(genreRows.getString("name")).build();
            genres.add(genre);
        }
        return genres;
    }

    @Override
    public Set<Genre> findForFilm(int filmId) {
        Set<Genre> genreSet = new LinkedHashSet<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT id, film_id, " +
                "genre_id FROM genre ORDER BY genre_id ASC");
        while (genreRows.next()) {
            if (genreRows.getLong("film_id") == filmId) {
                genreSet.add(findById(genreRows.getInt("genre_id")).get());
            }
        }
        return genreSet;
    }

    public void addGenreToFilm(Film film) {
        if (Objects.isNull(film.getGenres())) {
            return;
        }
        film.getGenres().forEach(genre -> genre.setName(findById(genre.getId()).get().getName()));
    }

    @Override
    public void addForFilm(Film film) {
        if (Objects.isNull(film.getGenres())) {
            return;
        }
        film.getGenres().forEach(g -> {
            String query = "INSERT INTO genre(film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.update(query,
                    film.getId(),
                    g.getId());
        });
    }

    @Override
    public void update(Film film) {
        String query = "DELETE FROM genre WHERE film_id = ?";
        jdbcTemplate.update(query, film.getId());
        addForFilm(film);
    }

    @Override
    public Optional<Genre> findById(int genreId) {
        String query = "SELECT genre_id, name FROM genre_type WHERE genre_id=?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, this::mapRowToGenre, genreId));
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
