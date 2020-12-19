package tech.itpark.creditsmanager.model;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Payment {
    @NonNull
    private Long id;
    private long paySum;
    private String payDate;
    private long mainDebt;
    private Long creditId;
}
