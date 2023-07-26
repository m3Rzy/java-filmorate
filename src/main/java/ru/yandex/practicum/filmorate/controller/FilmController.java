package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/films", produces = "application/json")
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        log.info("GET-запрос на получение всех фильмов.");
        return filmStorage.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable String id) {
        log.info("GET-запрос на получение фильма по id.");
        return filmStorage.getFilmById(Integer.parseInt(id));
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("POST-запрос на создание фильма.");
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("PUT-запрос на изменение существующего фильма.");
        return filmStorage.updateFilm(film);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") String count) {
        log.info("GET-запрос на получение популярных фильмов.");
        return filmService.getTopFilms(Integer.parseInt(count));
    }

    @PutMapping("/{id}/like/{filmId}")
    public void addLike(@PathVariable String id, @PathVariable String filmId) {
        log.info("PUT-запрос на добавление лайка к фильму по id.");
        filmService.addLike(Integer.parseInt(id), Integer.parseInt(filmId));
    }

    @DeleteMapping("/{id}/like/{filmId}")
    public void removeLike(@PathVariable String id, @PathVariable String filmId) {
        log.info("DELETE-запрос на удаление лайка у фильма по id.");
        filmService.removeLike(Integer.parseInt(id), Integer.parseInt(filmId));
    }

}
