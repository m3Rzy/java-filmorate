package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@DisplayName("Контроллер пользователя")
class UserControllerTest {

    private User user;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    private void start() {
        user = User.builder()
                .email("test@ya.ru")
                .login("admin")
                .name("Admin")
                .birthday(LocalDate.of(2002, 3, 4))
                .build();
    }

    @DisplayName("Добавление пользователя с корректными значениями")
    @Test
    void addNewUser_correct_isOkRequest() throws Exception {
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value("2"));
    }

    @DisplayName("Добавление пользователя с пустым именем")
    @Test
    void addNewUser_nameIsBlank_isOkRequest() throws Exception {
        user.setName("");
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value(""));
    }

    @DisplayName("Добавление пользователя с некорректным значением почты")
    @Test
    void addNewUser_incorrectEmail_isBadRequest() throws Exception {
        user.setEmail("incorrectEmail.ru");
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Добавление пользователя с пустым логином")
    @Test
    void addNewUser_incorrectLogin_isBadRequest() throws Exception {
        user.setLogin("");
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Добавление пользователя с др в будущем")
    @Test
    void addNewUser_incorrectDateInFuture_isBadRequest() throws Exception {
        user.setBirthday(LocalDate.parse("2044-12-12"));
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }
}