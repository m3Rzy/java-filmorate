package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.BadRequestException;
import ru.yandex.practicum.filmorate.util.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/films", produces = "application/json")
public class FilmController {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int filmId = 0;

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        filmValidation(film);
        film.setId(getIdFilm());
        films.put(film.getId(), film);
        log.info("Запрос на добавление фильма пройден.");
        return film;
    }

    @PutMapping
    public Film changeFilm(@Valid @RequestBody Film film) {
        if (films.get(film.getId()) != null) {
            filmValidation(film);
            films.put(film.getId(), film);
            log.info("Запрос на изменение фильма пройден.");
        } else {
            log.error("Запрос на изменении фильма не пройден.");
            throw new BadRequestException("Не удалось найти указанный фильм.");
        }
        return film;
    }


    private int getIdFilm() {
        return ++filmId;
    }

    private void filmValidation(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            throw new ValidationException("Некорректно указана дата релиза текущего фильма.");
        }
    }
}
