package tech.itpark.creditsmanager.model;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
public class Credit {
    private @NonNull int id;
    @NonNull
    private String name;
    @NonNull
    private long sum;
    private Date createdDate;
}
