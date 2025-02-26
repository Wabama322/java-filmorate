package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.utill.Reader;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
@Slf4j
@Component(value = "H2FilmDb")
@Repository
@RequiredArgsConstructor

public class FilmDbStorage implements FilmStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_QUERY_DIR = "src/main/resources/film/";
    private static final String SELECT_ALL_SQL_QUERY = Reader.readString(SQL_QUERY_DIR + "selectAll.sql");
    private static final String SELECT_BY_ID_SQL_QUERY = Reader.readString(SQL_QUERY_DIR + "selectById.sql");
    private static final String INSERT_SQL_QUERY = Reader.readString(SQL_QUERY_DIR + "insert.sql");
    private static final String UPDATE_SQL_QUERY = Reader.readString(SQL_QUERY_DIR + "update.sql");
    private static final String SELECT_POPULAR_FILMS_SQL_QUERY = Reader.readString(SQL_QUERY_DIR
            + "selectPopularFilms.sql");

    @Override
    public Film addFilm(Film object) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(INSERT_SQL_QUERY,
                            Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setString(2, object.getDescription());
            preparedStatement.setDate(3, Date.valueOf(object.getReleaseDate()));
            preparedStatement.setInt(4, object.getDuration());
            preparedStatement.setInt(5, object.getMpa().getId());
            return preparedStatement;
        }, keyHolder);
        return getFilm(keyHolder.getKey().intValue());
    }

    @Override
    public Film updateFilm(Film object) {
        jdbcTemplate.update(UPDATE_SQL_QUERY,
                object.getName(),
                object.getDescription(),
                Date.valueOf(object.getReleaseDate()),
                object.getDuration(),
                object.getMpa().getId(),
                object.getId());
        return getFilm(object.getId());
    }

    @Override
    public List<Film> getAllFilms() {
        return jdbcTemplate.query(SELECT_ALL_SQL_QUERY, new FilmMapper());
    }

    @Override
    public Film getFilm(Integer id) {
        return jdbcTemplate.query(SELECT_BY_ID_SQL_QUERY, new FilmMapper(), id).stream().findAny().orElse(null);
    }

    @Override
    public void deleteFilm(Film film) {
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return jdbcTemplate.query(SELECT_POPULAR_FILMS_SQL_QUERY, new FilmMapper(), count);
    }
}