package tech.itpark.creditsmanager.model;

import lombok.*;

import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Credit {
    @NonNull
    private long id;
    @NonNull
    private String name;
    @NonNull
    private long sum;
    private Date openDate;
    private int paymentDay;
    private Map<String, Long> payments;
}
