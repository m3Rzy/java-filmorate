package ru.yandex.practicum.filmorate.storage.mpa;

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

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестовое хранилище MpaDbStorageTest")
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MpaDbStorageTest {

    private final MpaDbStorage mpaDbStorage;
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
    }

    @DisplayName("Список всех рейтингов.")
    @Test
    void shouldGetMpas_isOkRequest() {
        assertEquals(5, mpaDbStorage.findAll().size());
        assertFalse(mpaDbStorage.findAll().isEmpty());
    }

    @DisplayName("Получение рейтинга по id.")
    @Test
    void shouldGetMpaById_isOkRequest() {
        assertEquals("PG-13", mpaDbStorage.findById(3).get().getName());
    }

    @DisplayName("Добавление рейтинга к фильму.")
    @Test
    void shouldAddMpaToFilm_isOkRequest() {
        assertNull(film.getMpa());
        film.setMpa(Mpa.builder()
                .id(10)
                .name("RR")
                .build());
        mpaDbStorage.add(film);
        assertNotNull(film.getMpa());
    }
}
