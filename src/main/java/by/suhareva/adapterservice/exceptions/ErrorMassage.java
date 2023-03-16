package by.suhareva.adapterservice.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "The entity of the error message for the client")
public class ErrorMassage {
    @Schema(description = "The full date the error occurred", example = "2023-03-15 11:36:24.07")
    private Date date;

    @Schema(description = "HTTP status", example = "404")
    private Integer status;

    @Schema(description = "ErrorMassage", example = "The Fine not found with number= 12AB123455")
    private String massage;

    @Schema(description = "Error reason", example = "FineNotFoundException")
    private String cause;

}
