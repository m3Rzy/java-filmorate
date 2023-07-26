package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FilmController.class)
@DisplayName("Контроллер фильма")
class FilmControllerTest {

    private Film film;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    protected void start() {
        film = Film.builder()
                .name("Фильм 1№")
                .description("Описание фильма")
                .releaseDate(LocalDate.of(2002, 3, 4))
                .duration(120)
                .build();
    }

    @DisplayName("Добавление фильма с корректными значениями")
    @Test
    void addNewFilm_correct_isOkRequest() throws Exception {
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value("1"));
    }

    @DisplayName("Добавление фильма с пустым значением имени")
    @Test
    void addNewFilm_incorrectName_isBadRequest() throws Exception {
        film.setName("");
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Добавление фильма с описанием больше 200 символов")
    @Test
    void addNewFilm_incorrectDescription_isBadRequest() throws Exception {
        film.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi " +
                "ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate " +
                "velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, " +
                "sunt in culpa qui officia deserunt mollit anim id est laborum.");
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Добавление фильма с релизом в будущем")
    @Test
    void addNewFilm_incorrectDateInFuture_isBadRequest() throws Exception {
        film.setReleaseDate(LocalDate.of(2044, 12, 12));
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Добавление фильма с релизом до 1885-12-9")
    @Test
    void addNewFilm_incorrectDateInPast_isBadRequest() throws Exception {
        film.setReleaseDate(LocalDate.of(1885, 12, 9));
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }
}