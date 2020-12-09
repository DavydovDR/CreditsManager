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
                "SELECT id, name, sum, createddate FROM credits ORDER BY id LIMIT 50",
                rowMapper);
    }

    public Credit getById(long id) {
        return template.queryForObject(
                "SELECT id, name, sum, createddate FROM credits WHERE id = :id",
                Map.of("id", id),
                rowMapper);
    }

    public Credit save(Credit item) {
        if (item.getId() == 0) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            template.update(
                    "INSERT INTO credits(name, sum, createddate) VALUES (:name, :sum, :createddate)",
                    new MapSqlParameterSource(Map.of(
                            "name", item.getName(),
                            "sum", item.getSum(),
                            "createddate", item.getCreatedDate()
                    )),
                    keyHolder
            );
            long id = keyHolder.getKey().longValue();
            return getById(id);
        }

        template.update(
                "UPDATE credits SET name = :name, sum = :sum WHERE id = :id",
                Map.of(
                        "id", item.getId(),
                        "name", item.getName(),
                        "sum", item.getSum(),
                        "createddate", item.getCreatedDate()
                )
        );

        return getById(item.getId());
    }

    public Credit removeById(long id) {
        Credit item = getById(id);

        template.update(
                "DELETE FROM credits WHERE id = :id",
                Map.of("id", item.getId())
        );

        return item;
    }
}
