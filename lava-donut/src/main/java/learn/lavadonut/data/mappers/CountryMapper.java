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
        country.setName(resultSet.getString("country_name"));
        country.setId(resultSet.getInt("country_id"));
        country.setCode(resultSet.getString("country_code"));

        CurrencyMapper currencyMapper = new CurrencyMapper();
        country.setCurrency(currencyMapper.mapRow(resultSet, i));

        return country;
    }
}
