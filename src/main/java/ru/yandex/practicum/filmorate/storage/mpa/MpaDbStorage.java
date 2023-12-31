package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> findAll() {
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

    @Override
    public Optional<Mpa> findById(int mpaId) {
        String query = "SELECT rating_mpa_id, name FROM mpa_type " +
                "WHERE rating_mpa_id=?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, this::mapRowToMpa, mpaId));
    }

    @Override
    public void add(Film film) {
        findAll().forEach(mpa -> {
            if (Objects.equals(film.getMpa().getId(), mpa.getId())) {
                film.setMpa(mpa);
            }
        });
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("rating_mpa_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
