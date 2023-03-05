package by.suhareva.adapterservice.integrationTest;

import by.suhareva.adapterservice.model.GetResponse;
import by.suhareva.adapterservice.model.SendRequest;
import by.suhareva.adapterservice.service.SyncImpl.AdapterServiceWebClientImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static by.suhareva.adapterservice.enums.ClientType.INDIVIDUAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@AutoConfigureMockMvc
public class WebclientTest extends AdapterControllerTest {
    private static MockWebServer mockWebServer;
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public void setWebClientBuilder(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
        this.webClientBuilder.baseUrl(mockWebServer.url("/").toString());
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdapterServiceWebClientImpl service;

    @BeforeAll
    public static void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(9090);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void getFine() throws Exception {
        UUID uuid = UUID.randomUUID();
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(objectMapper.writeValueAsString(uuid)));
        Mono<UUID> uuidMono = service.saveRequest(new SendRequest("12AA123455"));
        GetResponse responseTest = new GetResponse(UUID.randomUUID(), uuid, UUID.randomUUID(), "12AA123455", INDIVIDUAL, 123456, new Date(), new BigDecimal(2000.0), new BigDecimal(2000.0));
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(objectMapper.writeValueAsString(responseTest)));
        Mono<GetResponse> responseMono = service.getResponseByIdRequest(uuid);
        responseMono.flatMap(r -> {
            assertEquals(responseTest, r);
            return responseMono;
        });

        String message = "Response id=" + responseTest.getUuid() + " deleted";
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(objectMapper.writeValueAsString(message)));
        service.deleteResponse(responseTest.getUuid());

        mockMvc.perform(post("/adapter/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SendRequest("12AB123456"))))
                .andDo(print())
                .andReturn();
    }
}
