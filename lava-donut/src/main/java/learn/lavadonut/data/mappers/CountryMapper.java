package learn.lavadonut.data.mappers;

import learn.lavadonut.models.Country;
import learn.lavadonut.models.Currency;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryMapper implements RowMapper<Country> {
    @Override
    public Country mapRow(ResultSet resultSet, int i) throws SQLException {
        Country country = new Country();
        country.setName(resultSet.getString("name"));
        country.setId(resultSet.getInt("country_id"));
        country.setCode(resultSet.getString("code"));

        //TODO currency mapper or use an id
        // Solution: using a join so it directly comes in the resultSet
        Currency currency = new Currency();
        currency.setId(resultSet.getInt("currency_id"));
        currency.setCode(resultSet.getString("code"));
        currency.setName(resultSet.getString("name"));
        currency.setValueToUsd(resultSet.getBigDecimal("value_to_usd"));
        country.setCurrency(currency);

        // inner join
        /*

        select c.country_id, c.name, c.code, c.currency_id, cu.currency_code, cu.currency_name, cu.value_to_usd
        from countries c
        inner join currencies cu ON c.currency_id = cu.currency_id

         */
        return country;
    }
}
