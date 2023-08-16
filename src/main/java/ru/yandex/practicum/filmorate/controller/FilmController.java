package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
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
    public Film getFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Optional<Film> addFilm(@Valid @RequestBody Film film) {
        return Optional.ofNullable(filmService.addFilm(film));
    }

    @PutMapping
    public Optional<Film> updateFilm(@Valid @RequestBody Film film) {
        return Optional.ofNullable(filmService.updateFilm(film));
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

}
