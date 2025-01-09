package ru.yandex.practicum.filmorate.controllerTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserControllerTests {
    User user;

    @Test
    void validateNotCreateUserWithEmptyEmail() {
        user = User.builder()
                .email("")
                .login("Wabama")
                .name("Илья")
                .birthday(LocalDate.of(1993, 4, 12))
                .build();
        UserController userController = new UserController();
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
        UserController userController = new UserController();
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
        UserController userController = new UserController();
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.createUser(user);
        }, "Дата рождения из будущего");
    }

    @Test
    void validateCreateUserWithLoginInsteadOfName() {
        user = User.builder()
                .email("iliakuvshinov322@yandex.ru")
                .login("")
                .name("Wabama")
                .birthday(LocalDate.of(1993, 4, 12))
                .build();
        UserController userController = new UserController();
        User userCreate = userController.createUser(user);
        Assertions.assertEquals(userCreate.getName(), userCreate.getLogin(), "Присвоен login вместо имени");
    }
}
