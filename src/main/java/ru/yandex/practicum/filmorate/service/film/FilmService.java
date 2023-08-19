package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.util.NotFoundException;

import java.util.List;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film addFilm(Film film) {
        filmStorage.add(film);
        log.info("Фильм {} успешно создан.", film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (isExist(film.getId())) {
            filmStorage.update(film);
            log.info("Фильм {} успешно изменён.", film);
            return film;
        } else {
            throw new NotFoundException("FilmService.update | " + film + " не был найден.");
        }
    }

    public Film getFilmById(int id) {
        if (isExist(id)) {
            log.info("Фильм {} был успешно найден с помощью id.", id);
            return filmStorage.findById(id);
        } else {
            throw new NotFoundException("FilmService.getFilmById | Фильм с идентификатором " + id + " не был найден.");
        }
    }

    public List<Film> getFilms() {
        log.info("Количество фильмов в списке: " + filmStorage.findAll().size());
        return filmStorage.findAll();
    }

    public void addLike(int filmId, int userId) {
        if (isExist(filmId)) {
            log.info("К фильму с идентификатором {} был добавлен лайк.", filmId);
            filmStorage.like(filmId, userId);
        } else {
            throw new NotFoundException("FilmService.addLike | Фильм с идентификатором " + filmId + " не был найден.");
        }
    }

    public void removeLike(int filmId, int userId) {
        if (userService.getUserById(userId) == null) {
            throw new NotFoundException("FilmService.removeLike | Пользователь с идентификатором " + userId + " не был найден.");
        }
        if (isExist(filmId)) {
            filmStorage.removeLike(filmId, userId);
        } else {
            throw new NotFoundException("FilmService.removeLike | Фильм с идентификатором " + filmId + " не был найден.");
        }
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.getRating(count);
    }

    private boolean isExist(int id) {
        for (Film film : filmStorage.findAll()) {
            if (id == film.getId()) {
                return true;
            }
        }
        return false;
    }
}
