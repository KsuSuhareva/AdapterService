package by.suhareva.adapterservice.model;

import by.suhareva.adapterservice.enums.ClientType;
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
    private ClientType type;
    private Date resolution_date;
    private BigDecimal accrued;
    private  BigDecimal paid;
}
