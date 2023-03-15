package by.suhareva.adapterservice.integrationTest;

import by.suhareva.adapterservice.model.GetResponse;
import by.suhareva.adapterservice.model.SendRequest;
import by.suhareva.adapterservice.service.AdapterServiceSync;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static by.suhareva.adapterservice.enums.ClientType.INDIVIDUAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WebclientJsonTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    public static MockWebServer mockWebServer;
    @Autowired
    public ObjectMapper objectMapper;
    @Autowired
    private AdapterServiceSync service;


    @DynamicPropertySource
    static void backendProperties(DynamicPropertyRegistry registry) {
        registry.add("base-url", () -> mockWebServer.url("/").toString());
    }

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void getFineWebClientTest() throws Exception {
        UUID uuid = UUID.randomUUID();
        SendRequest testRequest = new SendRequest("12AA123456");
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(objectMapper.writeValueAsString(uuid)));
        Mono<SendRequest> request = service.saveRequest(testRequest);
        request.map(r -> {
            assertEquals(uuid, r.getUuid());
            return request;
        });
        testRequest.setUuid(uuid);
        GetResponse responseTest = new GetResponse(UUID.randomUUID(), uuid, UUID.randomUUID(), "12AA123455", INDIVIDUAL, 123456, new Date(), new BigDecimal(2000.0), new BigDecimal(2000.0));
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(objectMapper.writeValueAsString(responseTest)));
        Mono<GetResponse> responseMono = service.getResponseByIdRequest(testRequest);
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
        service.deleteResponse(responseTest);
    }

}
