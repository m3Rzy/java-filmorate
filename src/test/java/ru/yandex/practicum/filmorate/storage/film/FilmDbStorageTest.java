package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестовое хранилище FilmDbStorage")
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private Film film;
    private Film popularFilm;
    private User user;

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

        popularFilm = Film.builder()
                .name("Вий")
                .description("-")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();
        popularFilm.setGenres(new HashSet<>());
        popularFilm.setLikes(new HashSet<>());
        popularFilm.setMpa(Mpa.builder()
                .id(1)
                .name("PG-13")
                .build());

        user = User.builder()
                .name("Илья")
                .email("demeiz@yandex.ru")
                .login("demeiz")
                .birthday(LocalDate.of(2002, 4, 3))
                .build();
        user.setFriends(new HashSet<>());
    }

    void addEntities() {
        filmDbStorage.add(film);
        filmDbStorage.add(popularFilm);
        userDbStorage.add(user);
    }

    @DisplayName("Добавление нового фильма.")
    @Test
    void shouldAddNewFilm_isOkRequest() {
        filmDbStorage.add(film);
        assertEquals(film, filmDbStorage.findById(film.getId()).get());
    }

    @DisplayName("Добавление лайка на фильм.")
    @Test
    void shouldPressLike_isOkRequest() {
        addEntities();
        filmDbStorage.like(1, 1);
        assertEquals(filmDbStorage.findById(1).get().getLikes().size(), 1);
        assertFalse(filmDbStorage.findById(1).get().getLikes().isEmpty());
        assertTrue(filmDbStorage.findById(1).get().getLikes().contains(1));
    }

    @DisplayName("Удаление лайка с фильма.")
    @Test
    void shouldRemoveLike_isOkRequest() {
        addEntities();
        filmDbStorage.like(1, 1);
        filmDbStorage.removeLike(1, 1);
        assertEquals(filmDbStorage.findById(1).get().getLikes().size(), 0);
        assertFalse(filmDbStorage.findById(1).get().getLikes().contains(1));
        assertTrue(filmDbStorage.findById(1).get().getLikes().isEmpty());
    }

    @DisplayName("Изменение фильма.")
    @Test
    void shouldUpdateFilm_isOkRequest() {
        addEntities();
        String title = "Пустое название фильма.";
        film.setName(title);
        filmDbStorage.update(film);
        assertEquals(title, filmDbStorage.findById(1).get().getName());
        film.setDuration(555);
        filmDbStorage.update(film);
        assertEquals(555, filmDbStorage.findById(1).get().getDuration());
    }

    @DisplayName("Список популярных фильмов.")
    @Test
    void shouldGetPopularFilms_isOkRequest() {
        addEntities();
        filmDbStorage.like(2, 1);
        filmDbStorage.update(popularFilm);
        assertEquals(2, filmDbStorage.getRating(2).get(0).getId());
    }
}
