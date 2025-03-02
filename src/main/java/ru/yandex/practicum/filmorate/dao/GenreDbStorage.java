package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.utill.Reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Repository
@RequiredArgsConstructor

public class GenreDbStorage implements GenreDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_QUERY_DIR = "src/main/resources/genre/";
    private static final String SELECT_ALL_SQL_QUERY = Reader.readString(SQL_QUERY_DIR + "selectAll.sql");
    private static final String SELECT_FILM_GENRES_SQL_QUERY = Reader.readString(SQL_QUERY_DIR
            + "selectFilmGenres.sql");
    private static final String SELECT_GENRES_ALL_FILMS_SQL_QUERY = Reader.readString(SQL_QUERY_DIR
            + "selectGenresAllFilm.sql");
    private static final String DELETE_FILM_GENRES_SQL_QUERY = Reader.readString(SQL_QUERY_DIR
            + "deleteFilmGenres.sql");
    private static final String SELECT_BY_ID_SQL_QUERY = Reader.readString(SQL_QUERY_DIR + "selectById.sql");
    private static final String INSERT_SQL_QUERY = Reader.readString(SQL_QUERY_DIR + "insert.sql");

    @Override
    public List<Genre> getFilmGenres(Integer filmId) {
        return jdbcTemplate.query(SELECT_FILM_GENRES_SQL_QUERY, new GenreMapper(), filmId);
    }

    @Override
    public Map<Integer, List<Genre>> getAllFilmsGenres(List<Integer> filmIds) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("filmIds", filmIds);
        return namedParameterJdbcTemplate.query(SELECT_GENRES_ALL_FILMS_SQL_QUERY, parameters, rs -> {
            Map<Integer, List<Genre>> result = new HashMap<>();
            while (rs.next()) {
                int filmId = rs.getInt("film_id");
                Genre genre = new Genre(
                        rs.getInt("genre_id"),
                        rs.getString("genre_name")
                );
                result.computeIfAbsent(filmId, k -> new ArrayList<>()).add(genre);
            }
            return result;
        });
    }

    @Override
    public void setGenres(Integer filmId, Integer genreId) {
        jdbcTemplate.update(INSERT_SQL_QUERY, filmId, genreId);
    }

    @Override
    public List<Genre> getGenres() {
        return new ArrayList<>(jdbcTemplate.query(SELECT_ALL_SQL_QUERY, new GenreMapper()));
    }

    @Override
    public Genre getGenre(Integer genreId) {
        return jdbcTemplate.query(SELECT_BY_ID_SQL_QUERY, new GenreMapper(), genreId)
                .stream().findAny().orElse(null);
    }

    @Override
    public void clearFilmGenres(Integer filmId) {
        jdbcTemplate.update(DELETE_FILM_GENRES_SQL_QUERY, filmId);
    }
}