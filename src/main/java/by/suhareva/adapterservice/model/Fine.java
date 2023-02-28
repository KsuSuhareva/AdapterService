package by.suhareva.adapterservice.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Fine {
    private UUID id;
    private String number;
    private Integer resolution_num;
    private Date resolution_date;
    private BigDecimal accrued;
    private  BigDecimal paid;
}
