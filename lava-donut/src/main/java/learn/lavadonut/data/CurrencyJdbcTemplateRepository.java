package learn.lavadonut.data;

import learn.lavadonut.data.mappers.CurrencyMapper;
import learn.lavadonut.models.Currency;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class CurrencyJdbcTemplateRepository implements CurrencyRepository{
    private final JdbcTemplate jdbcTemplate;

    public CurrencyJdbcTemplateRepository(JdbcTemplate jdbcTemplate){ this.jdbcTemplate = jdbcTemplate; }

    public List<Currency> findAll(){
        final String sql = "select currency_id, `name`, `code`, value_to_usd "
                + "from currency;";
        return jdbcTemplate.query(sql, new CurrencyMapper());
    }

    public Currency findById(int currencyId){
        final String sql = "select currency_id, `name`, `code`, value_to_usd "
                + "from currency "
                + "where currency_id = ?;";

        return jdbcTemplate.query(sql, new CurrencyMapper(), currencyId)
                .stream()
                .findFirst().orElse(null);
    }

    public Currency add(Currency currency){
       final String sql = "insert into currencies (`name`, `code`, value_to_usd) "
               + "values (?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, currency.getName());
            ps.setString(2, currency.getCode());
            ps.setBigDecimal(3, currency.getValueToUsd());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        currency.setId(keyHolder.getKey().intValue());
        return currency;
    }

    public boolean update(Currency currency){
        return true;
    }

    public boolean delete(int currencyId){
        return true;
    }

}
