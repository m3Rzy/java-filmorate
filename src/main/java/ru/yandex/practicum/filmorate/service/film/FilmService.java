package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.util.NotFoundException;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.time.LocalDate;
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

    private void filmValidation(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))
                || film.getReleaseDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Некорректно указана дата релиза.");
        }
        if (film.getName().isEmpty()) {
            throw new ValidationException("Некорректно указано название фильма.");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Превышено количество символов в описании фильма.");
        }
    }

    public Film addFilm(Film film) {
        filmValidation(film);
        filmStorage.addFilmStorage(film);
        log.info("Фильм {} успешно создан.", film);
        return film;
    }

    public Film updateFilm(Film film) {
        try {
            filmValidation(film);
            filmStorage.updateFilmStorage(film);
            log.info("Фильм {} успешно изменён.", film);
            return film;
        } catch (RuntimeException e) {
            throw new NotFoundException("PUT - Фильма с id " + film.getId() + " не существует.");
        }
    }

    public Film getFilmById(int id) {
        try {
            filmStorage.findFilmById(id);
            log.info("Фильм {} был успешно найден с помощью id.", filmStorage.findFilmById(id));
            return filmStorage.findFilmById(id);
        } catch (RuntimeException e) {
            throw new NotFoundException("Такой фильм не найден.");
        }
    }

    public List<Film> getFilms() {
        log.info("Количество фильмов в списке: " + filmStorage.findFilms().size());
        return filmStorage.findFilms();
    }

    public void addLike(int filmId, int userId) {
        filmStorage.pressLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        if (userService.getUserById(userId) == null) {
            throw new NotFoundException("Пользователь не найден.");
        }
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.getRating(count);
    }
}
