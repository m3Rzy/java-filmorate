package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    //    список всех фильмов
    List<Film> findAll();

    //    получение фильма по id
    Film findById(int id);

    //    добавление нового фильма
    void add(Film film);

    //    изменение существующего фильма
    Film update(Film film);

    // присовение лайка фильму
    Film like(int filmId, int userId);

    // удаление лайка с фильма
    Film removeLike(int filmId, int userId);

    // список популярных фильмов
    List<Film> getRating(int limit);
}
