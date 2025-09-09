package learn.lavadonut.data;

import learn.lavadonut.data.mappers.CountryMapper;
import learn.lavadonut.models.Country;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CountryJdbcTemplateRepository implements CountryRepository{
    private final JdbcTemplate jdbcTemplate;

    public CountryJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Country> findAll() {

        final String sql = "select c.country_id, c.name as country_name, " +
                " c.code as country_code, c.currency_id, cu.`code` as currency_code, cu.`name` as currency_name, cu.value_to_usd " +

                "from countries c " +
                "inner join currencies cu on c.currency_id = cu.currency_id;";

        return jdbcTemplate.query(sql, new CountryMapper());
    }

    @Override
    public Country findById(int id) {

        final String sql = "select c.country_id, c.name as country_name, " +
                " c.code as country_code, c.currency_id, cu.`code` as currency_code, cu.`name` as currency_name, cu.value_to_usd " +
                "from countries c " +
                "inner join currencies cu on c.currency_id = cu.currency_id " +
                "where country_id = ?;";

        return jdbcTemplate.query(sql, new CountryMapper(), id).stream()
                .findFirst().orElse(null);
    }

    @Override
    public Country add(Country country) {
        final String sql = "insert into countries (`name`, `code`, currency_id) " +
                "values (?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, country.getName());
            ps.setString(2, country.getCode());
            ps.setInt(3, country.getCurrency().getId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        country.setId(keyHolder.getKey().intValue());
        return country;
    }

    @Override
    public boolean update(Country country) {
        final String sql = "update countries set " +
                "`name` = ?, " +
                "`code` = ?, " +
                "currency_id = ? " +
                "where country_id = ?;";

        return jdbcTemplate.update(sql,
                country.getName(), country.getCode(), country.getCurrency().getId(), country.getId()) > 0;
    }

    @Override
    public boolean delete(int id) {
        // get list of stocks dependent on country
        String sql = "select stock_id from stocks where country_id = ?;";
        List<Integer> stockIds =  jdbcTemplate.queryForList(sql, Integer.class, id);

        if (stockIds.isEmpty()) {
            String deleteCountrySql = "delete from countries where country_id = ?;";
            return jdbcTemplate.update(deleteCountrySql, id) > 0;
        }

        String commaSeparatedStockIds = stockIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        // check for count of orders dependent on stocks
        sql = "select count(*) from orders where stock_id in (?);";
        Integer orderCount = jdbcTemplate.queryForObject(sql, Integer.class, commaSeparatedStockIds);

        System.out.println(orderCount);
        if (orderCount != null && orderCount > 0) {
            return false; // cannot delete if there are dependent orders
        }

        sql = "delete from orders where stock_id in (?);";
        jdbcTemplate.update(sql, stockIds);

        sql = "delete from stocks where country_id = ?;";
        jdbcTemplate.update(sql, id);

        sql = "delete from countries where country_id = ?;";
        return jdbcTemplate.update(sql, id) > 0;
    }
}
