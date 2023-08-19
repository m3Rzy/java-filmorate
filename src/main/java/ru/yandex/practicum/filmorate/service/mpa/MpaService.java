package ru.yandex.practicum.filmorate.service.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.util.NotFoundException;

import java.util.List;

@Service
@Slf4j
public class MpaService {

    private final MpaStorage mpaDbStorage;

    public MpaService(MpaStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public List<Mpa> getMpas() {
        return mpaDbStorage.findAll();
    }

    public Mpa getMpaById(int ratingMpaId) {
        try {
            return mpaDbStorage.findById(ratingMpaId).get();
        } catch (RuntimeException e) {
            throw new NotFoundException("Рейтинг mpa не найден.");
        }
    }
}
