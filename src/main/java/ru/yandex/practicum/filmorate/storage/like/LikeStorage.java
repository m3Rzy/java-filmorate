package ru.yandex.practicum.filmorate.storage.like;

import java.util.Set;

public interface LikeStorage {

    Set<Integer> findAll(int filmId);
}
