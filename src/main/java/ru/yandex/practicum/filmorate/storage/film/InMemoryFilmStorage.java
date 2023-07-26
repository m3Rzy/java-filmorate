package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.BadRequestException;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else throw new BadRequestException("Фильм не был найден.");
    }

    private int getIdFilm() {
        return ++id;
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

    @Override
    public Film addFilm(Film film) {
        filmValidation(film);
        film.setLikes(new HashSet<>());
        film.setId(getIdFilm());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.get(film.getId()) != null) {
            filmValidation(film);
            film.setLikes(new HashSet<>());
            films.put(film.getId(), film);
        } else {
            throw new BadRequestException("Фильм не был найден.");
        }
        return film;
    }
}
