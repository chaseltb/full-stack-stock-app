package learn.lavadonut.data;

import learn.lavadonut.data.mappers.CurrencyMapper;
import learn.lavadonut.models.Currency;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CurrencyJdbcTemplateRepository implements CurrencyRepository{
    private final JdbcTemplate jdbcTemplate;

    public CurrencyJdbcTemplateRepository(JdbcTemplate jdbcTemplate){ this.jdbcTemplate = jdbcTemplate; }

    public List<Currency> findAll(){
        final String sql = "select currency_id, `name`, `code`, value_to_usd "
                + "from currency";
        return jdbcTemplate.query(sql, new CurrencyMapper());
    }

    public Currency findById(int currencyId){
        return null;
    }

    public Currency add(Currency currency){
        return null;
    }

    public boolean update(Currency currency){
        return true;
    }

    public boolean delete(int currencyId){
        return true;
    }

}
