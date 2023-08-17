package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    List<Genre> findGenres();

    Set<Genre> findGenresForCurrentFilm(int filmId);

    void addGenresForCurrentFilm(Film film);

    void updateGenresForCurrentFilm(Film film);

    Genre findGenreById(int genreId);
}
