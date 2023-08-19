package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    //    список всех фильмов
    List<Film> findAll();

    //    получение фильма по id
    Film findById(int id);

    //    добавление нового фильма
    void add(Film film);

    //    изменение существующего фильма
    Optional<Film> update(Film film);

    // присовение лайка фильму
    Optional<Film> like(int filmId, int userId);

    // удаление лайка с фильма
    Optional<Film> removeLike(int filmId, int userId);

    // список популярных фильмов
    List<Film> getRating(int limit);
}
