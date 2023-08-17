package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестовое хранилище GenreDbStorageTest")
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GenreDbStorageTest {

    private final GenreDbStorage genreDbStorage;
    private final FilmDbStorage filmDbStorage;
    private Film film;

    @BeforeEach
    void preparationEntities() {
        film = Film.builder()
                .name("Движение вверх")
                .description("Советским баскетболистам предстоит матч с непобедимыми соперниками." +
                        " Три секунды, вошедшие в историю спорта.")
                .releaseDate(LocalDate.of(2017, 12, 28))
                .duration(133)
                .build();
        film.setGenres(new HashSet<>());
        film.setLikes(new HashSet<>());
        film.setMpa(Mpa.builder()
                .id(1)
                .name("PG-13")
                .build());
    }

    @DisplayName("Список всех жанров.")
    @Test
    void shouldGetAllGenres_isOkRequest() {
        assertEquals(6, genreDbStorage.findGenres().size());
        assertEquals("Комедия", genreDbStorage.findGenres().get(0).getName());
        assertEquals("Боевик", genreDbStorage.findGenres().get(5).getName());
    }

    @DisplayName("Получение жанра по id.")
    @Test
    void shouldGetGenreById_isOkRequest() {
        assertEquals(1, genreDbStorage.findGenreById(1).getId());
        assertEquals("Комедия", genreDbStorage.findGenreById(1).getName());
    }

    @DisplayName("Добавление/изменение жанра/жанров к фильму.")
    @Test
    void shouldSetGenreForFilm_isOkRequest() {
        Set<Genre> genres = Set.of(Genre.builder()
                .id(6)
                .name("Боевик")
                .build(),
        Genre.builder()
                .id(1)
                .name("Комедия")
                .build());
        film.setGenres(genres);
        filmDbStorage.addFilmStorage(film);
        assertEquals(2, genreDbStorage.findGenresForCurrentFilm(1).size());
        assertEquals(genres, genreDbStorage.findGenresForCurrentFilm(1));
        assertTrue(genreDbStorage.findGenresForCurrentFilm(1).contains(genreDbStorage.findGenres().get(0)));
    }
}
