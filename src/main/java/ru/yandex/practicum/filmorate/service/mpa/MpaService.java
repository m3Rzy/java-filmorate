package ru.yandex.practicum.filmorate.service.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
@Slf4j
public class MpaService {

    private final MpaStorage mpaDbStorage;

    public MpaService(MpaStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public List<Mpa> getMpas() {
        return mpaDbStorage.findMpas();
    }

    public Mpa getMpaById(int ratingMpaId) {
        return mpaDbStorage.findMpaById(ratingMpaId);
    }
}
