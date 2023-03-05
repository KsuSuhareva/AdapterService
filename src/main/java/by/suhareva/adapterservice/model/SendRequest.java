package by.suhareva.adapterservice.model;

import by.suhareva.adapterservice.enums.ClientType;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SendRequest {

    private UUID uuid = null;
    @NotEmpty
    @Pattern(regexp = "[0-9]{2}[A-z,0-9]{2}[0-9]{6}|[0-9]{10}")
    private String number;
    private ClientType type;

    public SendRequest(String number) {
        this.number = number;
    }
}
