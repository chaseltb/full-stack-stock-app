package learn.lavadonut.data;


import learn.lavadonut.data.mappers.UserMapper;
import learn.lavadonut.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class UserJdbcRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        final String sql = "select user_id, currency_id, username, " +
                "first_name, last_name from user;";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    @Override
    public User findByUsername(String username) {
        final String sql = "select user_id, currency_id, username, first_name, " +
                "last_name from user where username = ?;";
        return jdbcTemplate.query(sql, new UserMapper(), username)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public User add(User user) {
        final String sql = "insert into user (currency_id, username, first_name, " +
                "last_name) values (?, ?, ?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, user.getCurrencyId());
            ps.setString(2, user.getUsername());

            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        user.setUserId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public boolean update(User user) {
        final String sql = "update user set " +
                "currency_id = ?, username = ?, " +
                "first_name = ?, last_name = ? where user_id = ?;";

        return jdbcTemplate.update(sql, user.getCurrencyId(), user.getUsername(),
                user.getFirstName(), user.getLastName(), user.getUserId()) > 0;
    }

    @Override
    public boolean deleteById(int userId) {
        final String sql = "delete from user where user_id = ?;";
        return jdbcTemplate.update(sql, userId) > 0;
    }
}
