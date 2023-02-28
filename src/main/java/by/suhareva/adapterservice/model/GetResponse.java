package by.suhareva.adapterservice.model;


import by.suhareva.adapterservice.exceptions.FineNotFoundException;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetResponse {
    private UUID uuid;
    private UUID uuid_request;
    private UUID id_fine;
    private String number;
    private Integer resolution_num;
    private Date resolution_date;
    private BigDecimal accrued;
    private BigDecimal paid;


    public Fine getFine(String number) throws FineNotFoundException {
        if (id_fine == null) {
            throw new FineNotFoundException("The Fine not found with number= " + number);
        }
        return new Fine(id_fine, this.number, resolution_num, resolution_date, accrued, paid);
    }

}
