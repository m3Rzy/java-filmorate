package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    //    список всех фильмов
    List<Film> getFilms();

    //    получение фильма по id
    Film getFilmById(int id);

    //    добавление нового фильма
    Film addFilm(Film film);

    //    изменение существующего фильма
    Film updateFilm(Film film);

}
