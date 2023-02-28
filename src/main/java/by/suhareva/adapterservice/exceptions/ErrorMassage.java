package by.suhareva.adapterservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Builder
public class ErrorMassage {
    private Date date;
    private Integer status;
    private String massage;
    private String cause;

}
