package ru.yandex.practicum.filmorate.controllerTests;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utill.Reader;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
public class UserControllerTests {
    User user;

    @Autowired
    private UserController userController;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void validateNotCreateUserWithEmptyEmail() {
        user = User.builder()
                .email("")
                .login("Wabama")
                .name("Илья")
                .birthday(LocalDate.of(1993, 4, 12))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.createUser(user);
        }, "Email не указан");
    }

    @Test
    void validateNotCreateUserWithEmptyLogin() {
        user = User.builder()
                .email("iliakuvshinov322@yandex.ru")
                .login("")
                .name("Илья")
                .birthday(LocalDate.of(1993, 4, 12))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.createUser(user);
        }, "Login не указан");
    }

    @Test
    void validateNotCreateUserWithWrongBirthday() {
        user = User.builder()
                .email("iliakuvshinov322@yandex.ru")
                .login("Wabama")
                .name("Илья")
                .birthday(LocalDate.of(2025, 4, 12))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.createUser(user);
        }, "Дата рождения из будущего");
    }

    @Test
    void validateCreateUserWithLoginInsteadOfName() {
        user = User.builder()
                .email("iliakuvshinov322@yandex.ru")
                .login("ilia")
                .name("")
                .birthday(LocalDate.of(1993, 4, 12))
                .build();
        User userCreate = userController.createUser(user);
        Assertions.assertEquals(userCreate.getName(), userCreate.getLogin(), "Присвоен Login вместо имени");
    }

    @BeforeEach
    public void beforeEach() {
        jdbcTemplate.update(Reader.readString("src/test/resources/drop.sql"));
        jdbcTemplate.update(Reader.readString("src/main/resources/schema.sql"));
    }
}
