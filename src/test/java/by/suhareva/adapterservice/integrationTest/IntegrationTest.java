package by.suhareva.adapterservice.integrationTest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class IntegrationTest {
    @Value("${svem.url.request.save}")
    public String REQUEST_SAVE_URL;

    @Value("${svem.url.response.get}")
    public String RESPONSE_GET_URL;

    @Value("${svem.url.response.delete}")
    public String RESPONSE_DELETE_URL;

    @Value("${adapter.url.fine.get}")
    public String FINE_GET_URL;

}
