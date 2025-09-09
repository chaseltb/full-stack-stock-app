package learn.lavadonut.data.mappers;

import learn.lavadonut.models.AssetType;
import learn.lavadonut.models.Stock;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StockMapper implements RowMapper<Stock> {


    @Override
    public Stock mapRow(ResultSet resultSet, int i) throws SQLException {
        Stock stock = new Stock();
        stock.setId(resultSet.getInt("stock_id"));
        stock.setName(resultSet.getString("stock_name"));
        stock.setTicker(resultSet.getString("ticker"));
        stock.setAssetType(AssetType.valueOf(resultSet.getString("asset_type")));
        stock.setIndustry(resultSet.getString("industry"));

        CountryMapper countryMapper = new CountryMapper();
        stock.setCountry(countryMapper.mapRow(resultSet, i));

        StockExchangeMapper stockExchangeMapper = new StockExchangeMapper();
        stock.setStockExchange(stockExchangeMapper.mapRow(resultSet,i));

        return stock;

    }
}
