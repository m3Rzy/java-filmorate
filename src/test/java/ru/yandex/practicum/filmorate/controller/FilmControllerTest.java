package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.time.LocalDate;


@DisplayName("Контроллер фильма")
class FilmControllerTest {

    private FilmStorage filmStorage;
    private FilmController filmController;
    private Film film;

    @BeforeEach
    protected void start() {
        filmStorage = new InMemoryFilmStorage();
        FilmService filmService = new FilmService(filmStorage);
        filmController = new FilmController(filmStorage, filmService);
        film = Film.builder()
                .name("Фильм №1")
                .description("Описание фильма")
                .releaseDate(LocalDate.of(2002, 4, 3))
                .duration(120)
                .build();
    }

    @DisplayName("Добавление фильма с корректными значениями")
    @Test
    void addNewFilm_correct_isOkRequest() {
        filmController.addFilm(film);
        Assertions.assertEquals(film, filmStorage.getFilmById(1));
    }

    @DisplayName("Добавление фильма с пустым значением имени")
    @Test
    void addNewFilm_incorrectName_isBadRequest() {
        film.setName("");
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            Assertions.assertEquals("Некорректно указано название фильма.", e.getMessage());
        }
    }

    @DisplayName("Добавление фильма с описанием больше 200 символов")
    @Test
    void addNewFilm_incorrectDescription_isBadRequest() {
        film.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi " +
                "ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate " +
                "velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, " +
                "sunt in culpa qui officia deserunt mollit anim id est laborum.");
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            Assertions.assertEquals("Превышено количество символов в описании фильма.", e.getMessage());
        }
    }

    @DisplayName("Добавление фильма с релизом в будущем")
    @Test
    void addNewFilm_incorrectDateInFuture_isBadRequest() {
        film.setReleaseDate(LocalDate.of(2044, 12, 12));
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            Assertions.assertEquals("Некорректно указана дата релиза.", e.getMessage());
        }
    }

    @DisplayName("Добавление фильма с релизом до 1885-12-9")
    @Test
    void addNewFilm_incorrectDateInPast_isBadRequest() {
        film.setReleaseDate(LocalDate.of(1885, 12, 9));
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            Assertions.assertEquals("Некорректно указана дата релиза.", e.getMessage());
        }
    }
}