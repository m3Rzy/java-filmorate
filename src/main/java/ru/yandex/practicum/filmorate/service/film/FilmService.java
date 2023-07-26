package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.util.BadRequestException;

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
        filmStorage.getFilmById(filmId).getLikes().add(userId);
    }

    public void removeLike(int filmId, int userId) {
        if (filmStorage.getFilmById(filmId).getLikes().contains(userId)) {
            filmStorage.getFilmById(filmId).getLikes().remove(userId);
        } else throw new BadRequestException("Пользователь не ставил лайк этому фильму.");
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.getFilms().stream()
                .sorted((a, b) -> b.getLikes().size() - a.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
