package learn.lavadonut.data.mappers;

import org.springframework.jdbc.core.RowMapper;
import learn.lavadonut.models.Currency;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyMapper implements RowMapper<Currency> {
    @Override
    public Currency mapRow(ResultSet resultSet, int i) throws SQLException{
        Currency currency = new Currency();

        currency.setId(resultSet.getInt("currency_id"));
        currency.setName(resultSet.getString("currency_name"));
        currency.setCode(resultSet.getString("currency_code"));
        currency.setValueToUsd(BigDecimal.valueOf(resultSet.getFloat("value_to_usd")));

        return currency;
    }
}
