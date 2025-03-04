package ru.yandex.practicum.filmorate.storageTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.utill.Reader;

import java.time.LocalDate;


@JdbcTest
@AutoConfigureTestDatabase
@Import(FilmDbStorage.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTests {

    private final FilmStorage filmStorage;
    private final JdbcTemplate jdbcTemplate;

    Film testFilm = new Film(null,
            "Красотка",
            "Про встречу бизнесмена и рыжей бестии",
            LocalDate.of(1994, 6, 24),
            220,
            0,
            new Rating(1, "G"),
            null);

    @BeforeEach
    void beforeEach() {
        jdbcTemplate.update(Reader.readString("src/test/resources/drop.sql"));
        jdbcTemplate.update(Reader.readString("src/main/resources/schema.sql"));
        jdbcTemplate.update(Reader.readString("src/test/resources/data.sql"));
    }

    @Test
    void getAllTest() {
        int[] idArray = filmStorage.getAllFilms()
                .stream().mapToInt(Film::getId).toArray();
        Assertions.assertArrayEquals(new int[]{1, 2, 3}, idArray);
    }

    @Test
    void getById() {
        Film film = new Film(1,
                "Собака-кусака",
                "Про самого отважного пса",
                LocalDate.of(1969, 4, 28),
                100,
                1,
                new Rating(3, "PG-13"),
                null);
        Assertions.assertEquals(film, filmStorage.getFilm(1));
    }

    @Test
    void addTest() {
        testFilm.setId(4);
        testFilm.setLikes(0);
        Assertions.assertEquals(testFilm, filmStorage.addFilm(testFilm));
    }

    @Test
    void updateTest() {
        testFilm.setId(1);
        testFilm.setLikes(1);
        Assertions.assertEquals(testFilm, filmStorage.updateFilm(testFilm));
    }
}
