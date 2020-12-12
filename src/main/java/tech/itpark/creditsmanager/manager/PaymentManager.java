package tech.itpark.creditsmanager.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import tech.itpark.creditsmanager.mapper.PaymentRowMapper;
import tech.itpark.creditsmanager.model.Credit;
import tech.itpark.creditsmanager.model.Payment;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentManager {
    private final NamedParameterJdbcTemplate template;
    private final PaymentRowMapper rowMapper = new PaymentRowMapper();

    public Payment getById(long id) {
        return template.queryForObject(
                "SELECT id, paysum, paydate, maindebt, ismade, creditid FROM payments WHERE id = :id",
                Map.of("id", id),
                rowMapper);
    }

    public List<Payment> getAll() {
        return template.query(
                "SELECT id, paysum, paydate, maindebt, ismade, creditid " +
                        "FROM payments ORDER BY paydate",
                rowMapper);
    }

    public List<Payment> getByCreditId(long creditId) {
        return template.query(
                "SELECT id, paysum, paydate, maindebt, ismade, creditid " +
                "FROM payments WHERE creditid = :creditid ORDER BY paydate",
                Map.of("creditid", creditId),
                rowMapper);
    }

    public Payment save(Payment item) {
        if (item.getId() == 0) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            template.update(
                    "INSERT INTO payments(paysum, paydate, maindebt, ismade, creditid) VALUES (:paysum, :paydate, :maindebt, :ismade, :creditid)",
                    new MapSqlParameterSource(Map.of(
                            "paysum", item.getPaySum(),
                            "paydate", item.getPayDate(),
                            "maindebt", item.getMainDebt(),
                            "ismade", item.isMade(),
                            "creditid", item.getCreditId()
                    )),
                    keyHolder
            );
            long id = keyHolder.getKey().longValue();
            return getById(id);
        }

        template.update(
                "UPDATE payments SET paysum = :paysum, paydate = :paydate, maindebt = :maindebt, ismade = :ismade, creditid = :creditid WHERE id = :id",
                Map.of(
                        "paysum", item.getPaySum(),
                        "paydate", item.getPayDate(),
                        "maindebt", item.getMainDebt(),
                        "ismade", item.isMade(),
                        "creditid", item.getCreditId()
                )
        );

        return getById(item.getId());
    }
}
