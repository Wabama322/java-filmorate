package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;

public interface GenreService {
    List<Genre> getGenres();

    Genre getGenre(Integer id);

    void setGenre(Integer idFilm, Integer idGenre);

    List<Genre> getFilmGenres(Integer filmId);

    void clearFilmGenres(Integer filmId);

    Map<Integer, List<Genre>> getAllFilmsGenres(List<Integer> filmIds);
}


