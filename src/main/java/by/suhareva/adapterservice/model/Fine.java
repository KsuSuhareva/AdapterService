package by.suhareva.adapterservice.model;

import by.suhareva.adapterservice.enums.ClientType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@XmlRootElement(name = "Fine")
@Schema(description = "The entity of the fine for the client")
public class Fine {
    @Schema(description = "Universally unique identifier", example = "3027ac9e-42cf-433a-8f02-2ecde80d352e")
    private UUID id;

    @Schema(description = "Number STS or  identification number of taxpayer", example = "12AB123456")
    private String number;

    @Schema(description = "Resolution number", example = "12345678")
    private Integer resolution_num;

    @Schema(description = "Сlient type", example = "INDIVIDUAL")
    private ClientType type;

    @Schema(description = "Resolution date", example = "2022-12-17 00:00:00")
    private Date resolution_date;

    @Schema(description = "The amount of the fine in Russian rubles", example = "1200")
    private BigDecimal accrued;

    @Schema(description = "The amount of the fine paid in Russian rubles", example = "1200")
    private BigDecimal paid;
}
