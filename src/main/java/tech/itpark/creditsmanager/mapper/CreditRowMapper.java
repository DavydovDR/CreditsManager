package tech.itpark.creditsmanager.mapper;

import org.springframework.jdbc.core.RowMapper;
import tech.itpark.creditsmanager.model.Credit;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CreditRowMapper implements RowMapper<Credit> {

    @Override
    public Credit mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Credit(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getBigDecimal("initial_debt"),
                resultSet.getBigDecimal("debt"),
                resultSet.getDate("created_date"),
                resultSet.getInt("pay_day"),
                resultSet.getBigDecimal("percent"),
                resultSet.getInt("initial_term"),
                resultSet.getInt("term"),
                resultSet.getBoolean("firstPayment")
        );
    }
}
