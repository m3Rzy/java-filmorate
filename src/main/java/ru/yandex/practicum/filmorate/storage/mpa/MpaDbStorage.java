package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.util.NotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> findMpas() {
        List<Mpa> mpas = new ArrayList<>();
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT rating_mpa_id, name FROM mpa_type");
        while (mpaRows.next()) {
            Mpa mpa = Mpa.builder()
                    .id(mpaRows.getInt("rating_mpa_id"))
                    .name(mpaRows.getString("name"))
                    .build();
            mpas.add(mpa);
        }
        return mpas;
    }

    public Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("rating_mpa_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    @Override
    public Mpa findMpaById(int mpaId) {
        String query = "SELECT rating_mpa_id, name FROM mpa_type " +
                "WHERE rating_mpa_id=?";
        try {
            return jdbcTemplate.queryForObject(query, this::mapRowToMpa, mpaId);
        } catch (RuntimeException e) {
            throw new NotFoundException("Рейтинг mpa не найден.");
        }
    }

    @Override
    public void addMpaToFilm(Film film) {
        findMpas().forEach(mpa -> {
            if (Objects.equals(film.getMpa().getId(), mpa.getId())) {
                film.setMpa(mpa);
            }
        });
    }
}
