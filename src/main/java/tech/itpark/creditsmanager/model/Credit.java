package tech.itpark.creditsmanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@AllArgsConstructor
public class Credit {
    @NonNull
    private int id;
    @NonNull
    private String name;
    @NonNull
    private BigDecimal initialDebt;
    @NonNull
    private BigDecimal debt;
    @NonNull
    private Date createdDate;
    @NonNull
    private int payDay;
    @NonNull
    private BigDecimal percent;
    @NonNull
    private int initialTerm;
    @NonNull
    private int term;
    @NonNull
    private boolean withoutFirstPayment;
}
