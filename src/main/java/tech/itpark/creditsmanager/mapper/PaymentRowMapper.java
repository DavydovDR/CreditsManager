package tech.itpark.creditsmanager.mapper;

import org.springframework.jdbc.core.RowMapper;
import tech.itpark.creditsmanager.model.Payment;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentRowMapper implements RowMapper<Payment> {
    @Override
    public Payment mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Payment(
                resultSet.getLong("id"),
                resultSet.getLong("paySum"),
                resultSet.getString("payDate"),
                resultSet.getLong("mainDebt"),
                resultSet.getLong("creditId")
        );
    }
}
