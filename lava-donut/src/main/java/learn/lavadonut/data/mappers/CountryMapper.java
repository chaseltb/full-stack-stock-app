package learn.lavadonut.data.mappers;

import learn.lavadonut.models.Country;
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
//        country.setCurrency();

        return country;
    }
}
