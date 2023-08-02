package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @Override
    public List<Film> findFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findFilmById(int id) {
        return films.get(id);
    }

    private int getIdFilm() {
        return ++id;
    }

    @Override
    public void addFilmStorage(Film film) {
        film.setLikes(new HashSet<>());
        film.setId(getIdFilm());
        films.put(film.getId(), film);
    }

    @Override
    public Film updateFilmStorage(Film film) {
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        return film;
    }
}
