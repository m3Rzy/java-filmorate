package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.util.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/films", produces = "application/json")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Optional<Film> getFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Optional<Film> addFilm(@Valid @RequestBody Film film) {
        filmValidation(film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Optional<Film> updateFilm(@Valid @RequestBody Film film) {
        filmValidation(film);
        return filmService.updateFilm(film);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getTopFilms(count);
    }

    @PutMapping("/{id}/like/{filmId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer filmId) {
        filmService.addLike(id, filmId);
    }

    @DeleteMapping("/{id}/like/{filmId}")
    public void removeLike(@PathVariable Integer id, @PathVariable Integer filmId) {
        filmService.removeLike(id, filmId);
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

}
