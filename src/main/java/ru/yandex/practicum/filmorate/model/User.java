package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class User {
    private Integer id;
    @Email(message = "Некорректно указан email.")
    @NonNull
    private String email;
    @NotBlank(message = "Логин не может быть пустым.")
    @Pattern(regexp = "\\S*", message = "Логин содержит лишние пробелы.")
    private String login;
    private String name;
    @NotNull
    @Past // TODO: реализовать собственную аннотацию для birthday
    private LocalDate birthday;
    private Set<Integer> friends;
}
