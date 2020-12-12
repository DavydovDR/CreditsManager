package tech.itpark.creditsmanager.mapper;

import org.springframework.jdbc.core.RowMapper;
import tech.itpark.creditsmanager.model.Credit;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CreditRowMapper implements RowMapper<Credit> {

    @Override
    public Credit mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Credit(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getLong("sum"),
                resultSet.getString("createdDate"),
                resultSet.getInt("payDay"),
                resultSet.getInt("percent")
        );
    }
}
