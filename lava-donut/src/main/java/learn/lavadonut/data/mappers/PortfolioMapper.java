package learn.lavadonut.data.mappers;

import learn.lavadonut.models.AccountType;
import learn.lavadonut.models.Portfolio;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PortfolioMapper implements RowMapper<Portfolio> {
    @Override
    public Portfolio mapRow(ResultSet resultSet, int i) throws SQLException {
        Portfolio portfolio = new Portfolio();
        portfolio.setId(resultSet.getInt("portfolio_id"));
        portfolio.setAccountType(AccountType.fromValue(resultSet.getString("account_type")));
        portfolio.setUserId(resultSet.getInt("user_id"));

        return portfolio;
    }
}
