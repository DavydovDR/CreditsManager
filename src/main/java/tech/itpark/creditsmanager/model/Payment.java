package tech.itpark.creditsmanager.model;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Payment {
    @NonNull
    private long id;
    private long paySum;
    private Date payDate;
    private boolean isMade;
    @NonNull
    private long creditId;

}
