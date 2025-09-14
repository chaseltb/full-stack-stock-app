package learn.lavadonut.data.mappers;

import learn.lavadonut.models.StockExchange;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StockExchangeMapper implements RowMapper<StockExchange> {
    @Override
    public StockExchange mapRow(ResultSet resultSet, int i) throws SQLException {
        StockExchange se = new StockExchange();

        se.setId(resultSet.getInt("stock_exchange_id"));
        se.setName(resultSet.getString("exchange_name"));
        se.setCode(resultSet.getString("exchange_code"));
        se.setTimeZone(resultSet.getString("timezone"));

        return se;
    }
}
