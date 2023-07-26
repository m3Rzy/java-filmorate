package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.util.BadRequestException;
import ru.yandex.practicum.filmorate.util.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/films", produces = "application/json")
public class FilmController {

    private final FilmStorage filmStorage;

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
}
