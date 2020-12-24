package tech.itpark.creditsmanager.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import tech.itpark.creditsmanager.mapper.PaymentRowMapper;
import tech.itpark.creditsmanager.model.Payment;
import tech.itpark.creditsmanager.service.CreditService;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PaymentManager {
    private final CreditService service;
    private final NamedParameterJdbcTemplate template;
    private final PaymentRowMapper rowMapper = new PaymentRowMapper();

    public Payment getById(int id) {
        return template.queryForObject(
                "SELECT id, amount, pay_date, main_debt, credit_id FROM payments WHERE id = :id",
                Map.of("id", id),
                rowMapper);
    }

    public List<Payment> getAll() {
        return template.query(
                "SELECT id, amount, pay_date, main_debt, credit_id FROM payments ORDER BY pay_date",
                rowMapper);
    }

    public List<Payment> getByCreditId(int creditId) {
        return template.query("SELECT id, amount, pay_date, main_debt, credit_id FROM payments WHERE credit_id = :creditId",
                Map.of("credit_id", creditId), rowMapper);
    }

    public Payment save(Payment item) {
        if (item.getId() == 0) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            template.update(
                    "INSERT INTO payments(amount, pay_date, main_debt, credit_id) VALUES (:amount, :pay_date, :main_debt, :credit_id)",
                    new MapSqlParameterSource(Map.of(
                            "amount",item.getAmount(),
                            "pay_date",item.getPayDate(),
                            "main_debt",item.getMainDebt(),
                            "credit_id", item.getCreditId()

                    )),
                    keyHolder
            );
            int id = keyHolder.getKey().intValue();
            return getById(id);
        }

        template.update(
                "UPDATE payments SET amount =:amount, pay_date =:pay_date, main_debt =:main_debt, credit_id =:credit_id WHERE id = :id",
                Map.of(
                        "amount",item.getAmount(),
                        "pay_date",item.getPayDate(),
                        "main_debt",item.getMainDebt(),
                        "credit_id", item.getCreditId()
                )
        );
        return getById(item.getId());
    }

    public List<Payment> getScheduleForTermByCreditId(int creditId) {

        var id = creditId;
        var yearPercent = template.queryForObject("SELECT percent FROM credits WHERE id = :id",
                Map.of("id", creditId),
                BigDecimal.class);
        var initialDebt = template.queryForObject("SELECT initial_debt FROM credits WHERE id = :id",
                Map.of("id", creditId),
                BigDecimal.class);
        var term = template.queryForObject("SELECT initial_term FROM credits WHERE id = :id",
                Map.of("id", creditId),
                Integer.class);
        var createdDate = template.queryForObject("SELECT created_date FROM credits WHERE id = :id",
                Map.of("id", creditId),
                Date.class).toLocalDate();
        var payDay = template.queryForObject("SELECT pay_day FROM credits WHERE id = :id",
                Map.of("id", creditId),
                Integer.class);
        var firstPayment = template.queryForObject("SELECT first_payment FROM credits WHERE id = :id",
                Map.of("id",creditId),
                Boolean.class);
        return service.getPaymentsSchedule(initialDebt, term, yearPercent, createdDate, payDay, firstPayment).
                stream().
                peek(p -> p.setCreditId(creditId)).
                collect(Collectors.toList());
    }

    public List<Payment> saveAllForCredit(List<Payment> items) {
        return items.stream().peek(this::save).collect(Collectors.toList());
    }

    public BigDecimal getAmountOfPercents(int id) {
        var paymentAmount = getByCreditId(id).stream().map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        var initialDebt = template.queryForObject("SELECT initial_debt FROM credits WHERE id = :id",
                Map.of("id", id),
                BigDecimal.class);
        return paymentAmount.divide(initialDebt);
    }

}
