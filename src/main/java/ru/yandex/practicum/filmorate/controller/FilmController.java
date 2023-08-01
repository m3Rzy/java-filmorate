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
        return filmStorage.findFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable String id) {
        return filmStorage.findFilmById(Integer.parseInt(id));
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getTopFilms(count);
    }

    @PutMapping("/{id}/like/{filmId}")
    public void addLike(@PathVariable String id, @PathVariable Integer filmId) {
        filmService.addLike(Integer.parseInt(id), filmId);
    }

    @DeleteMapping("/{id}/like/{filmId}")
    public void removeLike(@PathVariable String id, @PathVariable Integer filmId) {
        filmService.removeLike(Integer.parseInt(id), filmId);
    }

}
