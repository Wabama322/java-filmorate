package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;


@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    public static int count = 1;

    @Override
    public Film addFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        Film filmToUpdate = films.get(film.getId());
        if (isNull(filmToUpdate)) {
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
        }
        update(film, filmToUpdate);
        return filmToUpdate;
    }

    @Override
    public List<Film> getAllFilms() {
        Collection<Film> listOfFilms = films.values();
        return listOfFilms.stream().toList();
    }

    @Override
    public Film getFilm(Integer id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }
        return films.get(id);
    }

    @Override
    public void deleteFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм " + film + " не может быть удален, так как не существует ");
        }
        films.remove(film.getId());
    }

    private void update(Film film, Film filmToUpdate) {
        filmToUpdate.setName(film.getName());
        filmToUpdate.setDescription(film.getDescription());
        filmToUpdate.setDuration(film.getDuration());
        filmToUpdate.setReleaseDate(film.getReleaseDate());
    }

    private int generateId() {
        return count++;
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return films.values().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes(), f1.getLikes()))
                .limit(count)
                .collect(Collectors.toList());
    }
}