package tech.itpark.creditsmanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;

@Data
@AllArgsConstructor
public class Credit {
    @NonNull
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private Long sum;
    private String createdDate;
    private int payDay;
    @NonNull
    private Integer percent;
    private Integer months;
}
