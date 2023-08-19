package ru.yandex.practicum.filmorate.service.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.util.NotFoundException;

import java.util.List;

@Service
@Slf4j

public class GenreService {

    private final GenreStorage genreDbStorage;

    @Autowired
    public GenreService(GenreStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public Genre getGenre(int genreId) {
        try {
            return genreDbStorage.findById(genreId);
        } catch (RuntimeException e) {
            throw new NotFoundException("Жанр не найден.");
        }
    }

    public List<Genre> getGenres() {
        return genreDbStorage.findAll();
    }
}
