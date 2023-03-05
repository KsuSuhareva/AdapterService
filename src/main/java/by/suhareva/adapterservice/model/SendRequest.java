package by.suhareva.adapterservice.model;

import by.suhareva.adapterservice.enums.ClientType;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
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
@XmlRootElement( name = "SendRequest")
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
