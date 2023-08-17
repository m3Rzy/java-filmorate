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
    public List<Film> findFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findFilmById(int id) {
        return films.get(id);
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

    @Override
    public Film pressLike(int filmId, int userId) {
        findFilmById(filmId).getLikes().add(userId);
        return findFilmById(filmId);
    }

    @Override
    public Film removeLike(int filmId, int userId) {
        if (findFilmById(filmId).getLikes().contains(userId)) {
            findFilmById(filmId).getLikes().remove(userId);
        } else {
            throw new NotFoundException("Данный пользователь не ставил лайк этому фильму.");
        }
        return findFilmById(filmId);
    }

    @Override
    public List<Film> getRaing(int count) {
        return findFilms().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count).collect(Collectors.toList());
    }

    private int getIdFilm() {
        return ++id;
    }
}
