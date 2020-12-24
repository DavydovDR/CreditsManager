package tech.itpark.creditsmanager.model;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @NonNull
    private int id;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private Date payDate;
    @NonNull
    private BigDecimal mainDebt;
    @NonNull
    private int creditId;
}
