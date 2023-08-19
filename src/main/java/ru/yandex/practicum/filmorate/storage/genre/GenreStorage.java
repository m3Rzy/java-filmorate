package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    List<Genre> findAll();

    Set<Genre> findForFilm(int filmId);

    void addForFilm(Film film);

    void update(Film film);

    Genre findById(int genreId);
}
