package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findById(int id) {
        return films.get(id);
    }

    @Override
    public void add(Film film) {
        film.setLikes(new HashSet<>());
        film.setId(getIdFilm());
        films.put(film.getId(), film);
    }

    @Override
    public Film update(Film film) {
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film like(int filmId, int userId) {
        findById(filmId).getLikes().add(userId);
        return findById(filmId);
    }

    @Override
    public Film removeLike(int filmId, int userId) {
        if (findById(filmId).getLikes().contains(userId)) {
            findById(filmId).getLikes().remove(userId);
        } else {
            throw new NotFoundException("Данный пользователь не ставил лайк этому фильму.");
        }
        return findById(filmId);
    }

    @Override
    public List<Film> getRating(int limit) {
        return findAll().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(limit).collect(Collectors.toList());
    }

    private int getIdFilm() {
        return ++id;
    }
}
