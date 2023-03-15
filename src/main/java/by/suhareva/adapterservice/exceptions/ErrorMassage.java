package by.suhareva.adapterservice.exceptions;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@XmlRootElement(name  = "ErrorMassage")
@ToString
public class ErrorMassage {
    private Date date;
    private Integer status;
    private String massage;
    private String cause;

}
