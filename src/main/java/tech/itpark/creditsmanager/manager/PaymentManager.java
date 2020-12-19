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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PaymentManager {
    private final CreditService service;
    private final NamedParameterJdbcTemplate template;
    private final PaymentRowMapper rowMapper = new PaymentRowMapper();

    public Payment getById(long id) {
        return template.queryForObject(
                "SELECT id, paysum, paydate, maindebt, creditid FROM payments WHERE id = :id",
                Map.of("id", id),
                rowMapper);
    }

    public List<Payment> getAll() {
        return template.query(
                "SELECT id, paysum, paydate, maindebt, creditid " +
                        "FROM payments ORDER BY paydate",
                rowMapper);
    }

    public List<Payment> getByCreditId(long creditId) {
        return template.query("SELECT id, paysum, paydate, mainDebt, creditId " +
                "FROM payments WHERE creditId = :creditId",
                Map.of("creditId", creditId), rowMapper);
    }

    public Payment save(Payment item) {
        if (item.getId() == 0) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            template.update(
                    "INSERT INTO payments(paysum, paydate, maindebt, creditid) VALUES (:paySum, :payDate, :mainDebt, :creditId)",
                    new MapSqlParameterSource(Map.of(
                            "paySum", item.getPaySum(),
                            "payDate", item.getPayDate(),
                            "mainDebt", item.getMainDebt(),
                            "creditId", item.getCreditId()
                    )),
                    keyHolder
            );
            long id = keyHolder.getKey().longValue();
            return getById(id);
        }

        template.update(
                "UPDATE payments SET paysum = :paySum, paydate = :payDate, maindebt = :mainDebt, creditid = :creditId WHERE id = :id",
                Map.of(
                        "paySum", item.getPaySum(),
                        "payDate", item.getPayDate(),
                        "mainDebt", item.getMainDebt(),
                        "creditId", item.getCreditId()
                )
        );

        return getById(item.getId());
    }

    public List<Payment> getScheduleForTermByCreditId(long creditId) {

        var id = creditId;
        var yearPercent = template.queryForObject("SELECT percent FROM credits WHERE id = :id",
                Map.of("id", creditId),
                Integer.class);
        var sum = template.queryForObject("SELECT sum FROM credits WHERE id = :id",
                Map.of("id", creditId),
                Long.class);
        var months = template.queryForObject("SELECT months FROM credits WHERE id = :id",
                Map.of("id", creditId),
                Integer.class);
        var createdDate = template.queryForObject("SELECT createddate FROM credits WHERE id = :id",
                Map.of("id", creditId),
                String.class);
        var payDay = template.queryForObject("SELECT payday FROM credits WHERE id = :id",
                Map.of("id", creditId),
                Integer.class);
        return service.getPaymentsSchedule(sum, months, yearPercent, createdDate, payDay).
                stream().
                peek(p -> p.setCreditId(creditId)).peek(p -> p.setId((long) 0)).
                collect(Collectors.toList());
    }
}
