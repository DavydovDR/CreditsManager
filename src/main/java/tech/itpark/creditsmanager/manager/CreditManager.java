package tech.itpark.creditsmanager.manager;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import tech.itpark.creditsmanager.mapper.CreditRowMapper;
import tech.itpark.creditsmanager.model.Credit;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CreditManager {
    private final NamedParameterJdbcTemplate template;
    private final CreditRowMapper rowMapper = new CreditRowMapper();

    public List<Credit> getAll() {
        return template.query(
                "SELECT id, name, initial_debt, debt, created_date, pay_day, percent, initial_term, term, first_payment FROM credits ORDER BY id",
                rowMapper);
    }

    public Credit getById(int id) {
        return template.queryForObject(
                "SELECT id, name, initial_debt, debt, created_date, pay_day, percent, initial_term, term, first_payment FROM credits WHERE id = :id",
                Map.of("id", id),
                rowMapper);
    }

    public Credit save(Credit item) {
        if (item.getId() == 0) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            template.update("""
                            INSERT INTO credits(name, initial_debt, debt, created_date, pay_day, percent, initial_term, term, first_payment)
                            VALUES (:name, :initial_debt, :debt, :created_date, :pay_day, :percent, :initial_term, :term, :first_payment)""",
                    new MapSqlParameterSource(
                            Map.of("name", item.getName(),
                                    "initial_debt", item.getInitialDebt(),
                                    "debt", item.getDebt(),
                                    "created_date", item.getCreatedDate(),
                                    "pay_day", item.getPayDay(),
                                    "percent", item.getPercent(),
                                    "initial_term", item.getInitialTerm(),
                                    "term", item.getTerm(),
                                    "first_payment", item.isWithoutFirstPayment()
                            )
                    ), keyHolder
            );
            int id = keyHolder.getKey().intValue();
            return getById(id);
        }

        template.update("""
                        UPDATE credits SET name =:name, initial_debt =:initial_debt, debt =:debt, created_date =:created_date,
                        pay_day =:pay_day, percent =:percent, initial_term =:initial_term, term =:term, first_payment =:first_payment
                        WHERE id = :id""",
                Map.of(
                        "name", item.getName(),
                        "initial_debt", item.getInitialDebt(),
                        "debt", item.getDebt(),
                        "created_date", item.getCreatedDate(),
                        "pay_day", item.getPayDay(),
                        "percent", item.getPercent(),
                        "initial_term", item.getInitialTerm(),
                        "term", item.getTerm(),
                        "first_payment", item.isWithoutFirstPayment()
                )
        );
        return getById(item.getId());
    }

    public Credit removeById(int id) {
        Credit item = getById(id);
        template.update(
                "DELETE FROM credits WHERE id = :id",
                Map.of("id", item.getId())
        );
        return item;
    }
}
