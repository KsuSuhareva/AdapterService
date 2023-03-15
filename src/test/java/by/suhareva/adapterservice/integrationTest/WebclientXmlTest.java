package by.suhareva.adapterservice.integrationTest;

import by.suhareva.adapterservice.model.GetResponse;
import by.suhareva.adapterservice.model.SendRequest;
import by.suhareva.adapterservice.service.SyncImpl.AdapterServiceWebClientImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.Date;
import java.util.UUID;

import static by.suhareva.adapterservice.enums.ClientType.INDIVIDUAL;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class WebclientXmlTest extends IntegrationTest {
    private static MockWebServer mockWebServer;

    private XmlMapper mapperXml = new XmlMapper();
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdapterServiceWebClientImpl service;

    @BeforeAll
    public static void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(InetAddress.getByName("localhost"), 8092);
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
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_XML_VALUE)
                        .setBody(mapperXml.writeValueAsString(uuid)));
        SendRequest testRequest = new SendRequest("12AA123456");
        Mono<SendRequest> request = service.saveRequest(testRequest);
        request.flatMap(r -> {
            assertEquals(r.getUuid(), uuid);
            return request;
        });
        GetResponse responseTest = new GetResponse(UUID.randomUUID(), uuid, UUID.randomUUID(), "12AA123455", INDIVIDUAL, 123456, new Date(), new BigDecimal(2000.0), new BigDecimal(2000.0));
        testRequest.setUuid(uuid);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_XML_VALUE)
                        .setBody(mapperXml.writeValueAsString(responseTest)));
        Mono<GetResponse> responseMono = service.getResponseByIdRequest(testRequest);
        responseMono.flatMap(r -> {
            assertEquals(responseTest, r);
            return responseMono;
        });

        String message = "Response id=" + responseTest.getUuid() + " deleted";
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_XML_VALUE)
                        .setBody(mapperXml.writeValueAsString(message)));
        service.deleteResponse(responseTest);
    }
}
