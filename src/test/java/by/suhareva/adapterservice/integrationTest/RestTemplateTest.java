package by.suhareva.adapterservice.integrationTest;

import by.suhareva.adapterservice.model.Fine;
import by.suhareva.adapterservice.model.GetResponse;
import by.suhareva.adapterservice.model.SendRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static by.suhareva.adapterservice.enums.ClientType.INDIVIDUAL;
import static by.suhareva.adapterservice.enums.ClientType.LEGAL_ENTITY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class RestTemplateTest extends IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;


    @BeforeEach
    public void createMock() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @AfterEach
    public void verifyMock() {
        mockServer.verify();
    }

    @Test
    public void getFineWithValidDateForIndividual() throws Exception {
        SendRequest request = new SendRequest(null, "12AA123456", INDIVIDUAL);
        UUID uuid = UUID.randomUUID();
        mockServer.expect(once(), requestTo(REQUEST_SAVE_URL))
                .andRespond(withStatus(HttpStatus.ACCEPTED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(uuid)));
        GetResponse response = new GetResponse(UUID.randomUUID(), uuid, UUID.randomUUID(), "12AA123456", INDIVIDUAL, 123456, new Date(), new BigDecimal(2000.0), new BigDecimal(2000.0));
        mockServer.expect(once(), requestTo(RESPONSE_GET_URL))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(response)));
        mockServer.expect(once(), requestTo(RESPONSE_DELETE_URL))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString("Response " + response.getUuid() + "deleted")));
        MvcResult mvcResult = mockMvc.perform(post(FINE_GET_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andDo(print())
                .andReturn();
        Fine fine = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                Fine.class);
        assertEquals(request.getNumber(), fine.getNumber());
        assertEquals(request.getType(), fine.getType());
    }


    @Test
    public void getFineWithInValidDateForIndividual() throws Exception {
        SendRequest request = new SendRequest(null, "12AA123", INDIVIDUAL);
        mockMvc.perform(post("/adapter/getFineRestTemplate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.cause").value("MethodArgumentNotValidException"))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void getFineWithValidDateForLegalEntity() throws Exception {
        SendRequest request = new SendRequest(null, "1234567890", LEGAL_ENTITY);
        UUID uuid = UUID.randomUUID();
        mockServer.expect(once(), requestTo("http://localhost:8091/request/save/"))
                .andRespond(withStatus(HttpStatus.ACCEPTED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(uuid)));
        GetResponse response = new GetResponse(UUID.randomUUID(), uuid, UUID.randomUUID(), "1234567890", LEGAL_ENTITY, 123456, new Date(), new BigDecimal(2000.0), new BigDecimal(2000.0));
        mockServer.expect(once(), requestTo("http://localhost:8091/request/getResponse/"))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(response)));
        mockServer.expect(once(), requestTo("http://localhost:8091/request/delete/"))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString("Response " + response.getUuid() + "deleted")));
        MvcResult mvcResult = mockMvc.perform(post("/adapter/getFineRestTemplate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andDo(print())
                .andReturn();
        Fine fine = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                Fine.class);
        assertEquals(request.getNumber(), fine.getNumber());
        assertEquals(request.getType(), fine.getType());
    }

    @Test
    public void getFineWithInValidDateForLegalEntity() throws Exception {
        SendRequest request = new SendRequest(null, "12hfhf123", INDIVIDUAL);
        mockMvc.perform(post("/adapter/getFineRestTemplate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.cause").value("MethodArgumentNotValidException"))
                .andDo(print())
                .andReturn();
    }

}

