package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    //    список всех фильмов
    List<Film> findFilms();

    //    получение фильма по id
    Film findFilmById(int id);

    //    добавление нового фильма
    void addFilmStorage(Film film);

    //    изменение существующего фильма
    Film updateFilmStorage(Film film);

    // присовение лайка фильму
    Film pressLike(int filmId, int userId);

    // удаление лайка с фильма
    Film removeLike(int filmId, int userId);

    // список популярных фильмов
    List<Film> getRaing(int count);
}
