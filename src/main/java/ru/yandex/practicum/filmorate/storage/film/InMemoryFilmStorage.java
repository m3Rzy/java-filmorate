package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.NotFoundException;

import java.util.*;
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
    public Optional<Film> findById(int id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public void add(Film film) {
        film.setLikes(new HashSet<>());
        film.setId(getIdFilm());
        films.put(film.getId(), film);
    }

    @Override
    public Optional<Film> update(Film film) {
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        return Optional.of(film);
    }

    @Override
    public Optional<Film> like(int filmId, int userId) {
        findById(filmId).get().getLikes().add(userId);
        return Optional.of(findById(filmId).get());
    }

    @Override
    public Optional<Film> removeLike(int filmId, int userId) {
        if (findById(filmId).get().getLikes().contains(userId)) {
            findById(filmId).get().getLikes().remove(userId);
        } else {
            throw new NotFoundException("Данный пользователь не ставил лайк этому фильму.");
        }
        return Optional.of(findById(filmId).get());
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
