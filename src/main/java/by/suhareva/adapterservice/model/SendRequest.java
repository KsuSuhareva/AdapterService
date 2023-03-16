package by.suhareva.adapterservice.model;

import by.suhareva.adapterservice.enums.ClientType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@XmlRootElement(name = "SendRequest")
@Schema(description = "The entity of the request to get the fine")
public class SendRequest {

    @Schema(description = "Universally unique identifier", example = "3027ac9e-42cf-433a-8f02-2ecde80d352e")
    private UUID uuid;
    @NotEmpty
    @Pattern(regexp = "[0-9]{2}[A-z,0-9]{2}[0-9]{6}|[0-9]{10}")
    @Schema(description = "Number STS or  identification number of taxpayer", example = "12AB123456")
    private String number;
    @Schema(description = "Сlient type ", example = "INDIVIDUAL")
    private ClientType type;

    public SendRequest(String number) {
        this.number = number;
    }
}
