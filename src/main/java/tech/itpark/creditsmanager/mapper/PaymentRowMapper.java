package tech.itpark.creditsmanager.mapper;

import org.springframework.jdbc.core.RowMapper;
import tech.itpark.creditsmanager.model.Payment;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentRowMapper implements RowMapper<Payment> {
    @Override
    public Payment mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Payment(
                resultSet.getInt("id"),
                resultSet.getBigDecimal("amount"),
                resultSet.getDate("pay_date"),
                resultSet.getBigDecimal("main_debt"),
                resultSet.getInt("credit_id")
        );
    }
}
