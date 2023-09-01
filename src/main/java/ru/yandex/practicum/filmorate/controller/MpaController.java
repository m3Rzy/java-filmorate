package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mpa", produces = "application/json")
public class MpaController {

    private final MpaService mpaService;

    @GetMapping
    public List<Mpa> getMpas() {
        return mpaService.getMpas();
    }

    @GetMapping("/{id}")
    public Optional<Mpa> getMpaRating(@PathVariable("id") int mpaId) {
        return mpaService.getMpaById(mpaId);
    }
}
