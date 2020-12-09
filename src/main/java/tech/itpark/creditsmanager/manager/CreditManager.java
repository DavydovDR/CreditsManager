package tech.itpark.creditsmanager.manager;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import tech.itpark.creditsmanager.mapper.CreditRowMapper;
import tech.itpark.creditsmanager.model.Credit;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreditManager {
    private final NamedParameterJdbcTemplate template;
    private final CreditRowMapper rowMapper = new CreditRowMapper();

    public List<Credit> getAll() {
        return template.query(
                "SELECT id, name, sum FROM credits ORDER BY id LIMIT 50",
                rowMapper);
    }
}
