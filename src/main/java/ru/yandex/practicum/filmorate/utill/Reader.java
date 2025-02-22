package ru.yandex.practicum.filmorate.utill;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

public final class Reader {
    public Reader() {
        throw new RuntimeException("Attempt to create a final class");
    }

    public static String readString(String filePath) throws NoSuchElementException {
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException ioException) {
            throw new RuntimeException();
        }
    }
}
