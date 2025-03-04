package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utill.Reader;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_QUERY_DIR = "src/main/resources/user/friend/";
    private static final String SELECT_ALL_SQL_QUERY = Reader.readString(SQL_QUERY_DIR + "selectAll.sql");
    private static final String SELECT_COMMON_SQL_QUERY = Reader.readString(
            SQL_QUERY_DIR + "selectCommon.sql");
    private static final String SELECT_CONFIRMING_STATUS_SQL_QUERY = Reader.readString(
            SQL_QUERY_DIR + "selectConfirmingStatus.sql");
    private static final String INSERT_SQL_QUERY = Reader.readString(SQL_QUERY_DIR + "insert.sql");
    private static final String DELETE_SQL_QUERY = Reader.readString(SQL_QUERY_DIR + "delete.sql");

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(Integer userIdOne, Integer userIdTwo) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(INSERT_SQL_QUERY,
                            Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, userIdOne);
            preparedStatement.setInt(2, userIdTwo);
            preparedStatement.setBoolean(3, isFriend(userIdTwo, userIdOne));
            return preparedStatement;
        }, keyHolder);
    }

    @Override
    public void deleteFriend(Integer userIdOne, Integer userIdTwo) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(DELETE_SQL_QUERY,
                            Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, userIdOne);
            preparedStatement.setInt(2, userIdTwo);
            return preparedStatement;
        }, keyHolder);
    }

    @Override
    public Boolean isFriend(Integer userIdOne, Integer userIdTwo) {
        return jdbcTemplate.query(SELECT_CONFIRMING_STATUS_SQL_QUERY,
                        (rs, rowNum) -> rs.getObject("status", Boolean.class), userIdOne, userIdTwo, userIdTwo, userIdOne)
                .stream().anyMatch(Objects::nonNull);
    }

    @Override
    public List<User> getAllUserFriends(Integer userId) {
        return jdbcTemplate.query(SELECT_ALL_SQL_QUERY, new UserMapper(), userId);
    }

    @Override
    public List<User> getMutualFriends(Integer userIdOne, Integer userIdTwo) {
        return jdbcTemplate.query(SELECT_COMMON_SQL_QUERY, new UserMapper(), userIdOne, userIdTwo);
    }
}