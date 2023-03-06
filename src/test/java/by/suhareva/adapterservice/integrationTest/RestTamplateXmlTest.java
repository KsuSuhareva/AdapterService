package by.suhareva.adapterservice.integrationTest;

import by.suhareva.adapterservice.model.Fine;
import by.suhareva.adapterservice.model.GetResponse;
import by.suhareva.adapterservice.model.SendRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestTamplateXmlTest extends IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    private XmlMapper mapperXml = new XmlMapper();
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
    public void getFineWithValidDateXmlFormatForIndividual() throws Exception {
        SendRequest request = new SendRequest();
        request.setNumber("12AA123456");
        request.setType(INDIVIDUAL);
        UUID uuid = UUID.randomUUID();

        mockServer.expect(once(), requestTo("http://localhost:8091/request/save/"))
                .andRespond(withStatus(HttpStatus.ACCEPTED)
                        .contentType(MediaType.TEXT_XML)
                        .body(mapperXml.writeValueAsString(uuid)));
        GetResponse response = new GetResponse(UUID.randomUUID(), uuid, UUID.randomUUID(), "12AA123456", INDIVIDUAL, 123456, new Date(), new BigDecimal(2000.0), new BigDecimal(2000.0));
        mockServer.expect(once(), requestTo("http://localhost:8091/request/getResponse/"))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.TEXT_XML)
                        .body(mapperXml.writeValueAsString(response)));
        mockServer.expect(once(), requestTo("http://localhost:8091/request/delete/"))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.TEXT_XML)
                        .body(mapperXml.writeValueAsString("Response " + response.getUuid() + "deleted")));
        MvcResult mvcResult = mockMvc.perform(post("/adapter/getFineRestTemplate")
                        .accept(MediaType.TEXT_XML_VALUE)
                        .contentType(MediaType.TEXT_XML_VALUE)
                        .content(mapperXml.writeValueAsString(request)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType("text/xml;charset=UTF-8"))
                .andDo(print())
                .andReturn();
        Fine fine = mapperXml.readValue(
                mvcResult.getResponse().getContentAsString(),
                Fine.class);
        assertEquals(request.getNumber(), fine.getNumber());
        assertEquals(request.getType(), fine.getType());
    }

    @Test
    public void getFineWithValidDateXmlFormatForLegalEntity() throws Exception {
        SendRequest request = new SendRequest();
        request.setNumber("1234567890");
        request.setType(LEGAL_ENTITY);
        UUID uuid = UUID.randomUUID();

        mockServer.expect(once(), requestTo("http://localhost:8091/request/save/"))
                .andRespond(withStatus(HttpStatus.ACCEPTED)
                        .contentType(MediaType.TEXT_XML)
                        .body(mapperXml.writeValueAsString(uuid)));
        GetResponse response = new GetResponse(UUID.randomUUID(), uuid, UUID.randomUUID(), "1234567890", LEGAL_ENTITY, 123456, new Date(), new BigDecimal(2000.0), new BigDecimal(2000.0));
        mockServer.expect(once(), requestTo("http://localhost:8091/request/getResponse/"))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.TEXT_XML)
                        .body(mapperXml.writeValueAsString(response)));
        mockServer.expect(once(), requestTo("http://localhost:8091/request/delete/"))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.TEXT_XML)
                        .body(mapperXml.writeValueAsString("Response " + response.getUuid() + "deleted")));
        MvcResult mvcResult = mockMvc.perform(post("/adapter/getFineRestTemplate")
                        .accept(MediaType.TEXT_XML_VALUE)
                        .contentType(MediaType.TEXT_XML_VALUE)
                        .content(mapperXml.writeValueAsString(request)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType("text/xml;charset=UTF-8"))
                .andDo(print())
                .andReturn();
        Fine fine = mapperXml.readValue(
                mvcResult.getResponse().getContentAsString(),
                Fine.class);
        assertEquals(request.getNumber(), fine.getNumber());
        assertEquals(request.getType(), fine.getType());
    }
}
