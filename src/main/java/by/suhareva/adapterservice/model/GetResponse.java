package by.suhareva.adapterservice.model;


import by.suhareva.adapterservice.enums.ClientType;
import by.suhareva.adapterservice.exceptions.FineNotFoundException;
import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@XmlRootElement( name = "GetResponse")
public class GetResponse {
    private UUID uuid;
    private UUID uuid_request;
    private UUID id_fine;
    private String number;
    private ClientType type;
    private Integer resolution_num;
    private Date resolution_date;
    private BigDecimal accrued;
    private BigDecimal paid;


    public Fine getFine(String number) throws FineNotFoundException {
        if (id_fine == null) {
            throw new FineNotFoundException("The Fine not found with number= " + number);
        }
        return new Fine(id_fine, this.number, resolution_num,this.type, resolution_date, accrued, paid);
    }

}
