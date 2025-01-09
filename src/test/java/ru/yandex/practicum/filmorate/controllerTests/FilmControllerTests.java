package ru.yandex.practicum.filmorate.controllerTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmControllerTests {
    Film film;

    @Test
    void validateNotAddFilmWithEmptyName() {
        film = Film.builder()
                .name("")
                .description("Бэтмен поднимает ставки в войне с криминалом. С помощью лейтенанта Джима Гордона и " +
                        "прокурора Харви Дента он намерен очистить улицы Готэма от преступности.")
                .releaseDate(LocalDate.of(2008, 8, 14))
                .duration(152)
                .build();
        FilmController filmController = new FilmController();
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        }, "Пустое название фильма");
    }

    @Test
    void validateNotAddFilmWithLongDescription() {
        film = Film.builder()
                .name("Темный рыцарь")
                .description("Бэтмен поднимает ставки в войне с криминалом. С помощью лейтенанта Джима Гордона и " +
                        "прокурора Харви Дента он намерен очистить улицы Готэма от преступности. Сотрудничество " +
                        "оказывается эффективным, но скоро они обнаружат себя посреди хаоса, развязанного восходящим " +
                        "криминальным гением, известным напуганным горожанам под именем Джокер..")
                .releaseDate(LocalDate.of(2008, 8, 14))
                .duration(152)
                .build();
        FilmController filmController = new FilmController();
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        }, "Описание превышает 200 символов");
    }

    @Test
    void validateNotAddFilmWithReleaseDateLaterThanNow() {
        film = Film.builder()
                .name("Темный рыцарь")
                .description("Бэтмен поднимает ставки в войне с криминалом. С помощью лейтенанта Джима Гордона и " +
                        "прокурора Харви Дента он намерен очистить улицы Готэма от преступности.")
                .releaseDate(LocalDate.of(1894, 12, 28))
                .duration(152)
                .build();
        FilmController filmController = new FilmController();
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        }, "Фильм создан ранее,чем 1895.12.28");
    }

    @Test
    void validateNotAddFilmIfDurationEqualsZero() {
        film = Film.builder()
                .name("Темный рыцарь")
                .description("Бэтмен поднимает ставки в войне с криминалом. С помощью лейтенанта Джима Гордона и " +
                        "прокурора Харви Дента он намерен очистить улицы Готэма от преступности.")
                .releaseDate(LocalDate.of(2008, 8, 14))
                .duration(0)
                .build();
        FilmController filmController = new FilmController();
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        }, "Продолжительность фильма не может быть равна нулю");
    }
}
