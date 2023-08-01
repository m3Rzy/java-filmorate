package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.util.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int filmId, int userId) {
        filmStorage.findFilmById(filmId).getLikes().add(userId);
    }

    public void removeLike(int filmId, int userId) {
        if (filmStorage.findFilmById(filmId).getLikes().contains(userId)) {
            filmStorage.findFilmById(filmId).getLikes().remove(userId);
        } else throw new NotFoundException("Пользователь не ставил лайк этому фильму.");
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.findFilms().stream()
                .sorted((a, b) -> b.getLikes().size() - a.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
