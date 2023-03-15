package by.suhareva.adapterservice.integrationTest;

import by.suhareva.adapterservice.integrationTest.MaperXmlJAXB.MapperXmlJAXB;
import by.suhareva.adapterservice.model.Fine;
import by.suhareva.adapterservice.model.GetResponse;
import by.suhareva.adapterservice.model.SendRequest;
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

public class RestTemplateXmlTest extends IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    private XmlMapper mapperXml = new XmlMapper();
    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    public RestTemplateXmlTest() {
    }

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
        SendRequest request = new SendRequest(UUID.randomUUID(), "12AA123456", INDIVIDUAL);
        mockServer.expect(once(), requestTo(REQUEST_SAVE_URL))
                .andRespond(withStatus(HttpStatus.ACCEPTED)
                        .contentType(MediaType.APPLICATION_XML)
                        .body(MapperXmlJAXB.writeValueAsString(request)));

        GetResponse response = new GetResponse(UUID.randomUUID(), request.getUuid(), UUID.randomUUID(), "12AA123456", INDIVIDUAL, 123456, new Date(), new BigDecimal(2000.0), new BigDecimal(2000.0));
        mockServer.expect(once(), requestTo(RESPONSE_GET_URL))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_XML)
                        .body(MapperXmlJAXB.writeValueAsString(response)));

        mockServer.expect(once(), requestTo(RESPONSE_DELETE_URL))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_XML)
                        .body(mapperXml.writeValueAsString("Response " + response.getUuid() + "deleted")));

        MvcResult mvcResult = mockMvc.perform(post(FINE_GET_URL)
                        .accept(MediaType.TEXT_XML_VALUE)
                        .contentType(MediaType.APPLICATION_XML)
                        .content(MapperXmlJAXB.writeValueAsString(request)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType("text/xml;charset=UTF-8"))
                .andDo(print())
                .andReturn();
        Fine fine = MapperXmlJAXB.readValueAsString(
                mvcResult.getResponse().getContentAsString(),
                Fine.class);
        assertEquals(request.getNumber(), fine.getNumber());
        assertEquals(request.getType(), fine.getType());
    }

    @Test
    public void getFineWithValidDateXmlFormatForLegalEntity() throws Exception {
        SendRequest request = new SendRequest(UUID.randomUUID(), "1234567890", LEGAL_ENTITY);
        mockServer.expect(once(), requestTo(REQUEST_SAVE_URL))
                .andRespond(withStatus(HttpStatus.ACCEPTED)
                        .contentType(MediaType.APPLICATION_XML)
                        .body(MapperXmlJAXB.writeValueAsString(request)));
        GetResponse response = new GetResponse(UUID.randomUUID(), request.getUuid(), UUID.randomUUID(), "1234567890", LEGAL_ENTITY, 123456, new Date(), new BigDecimal(2000.0), new BigDecimal(2000.0));
        mockServer.expect(once(), requestTo(RESPONSE_GET_URL))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_XML)
                        .body(MapperXmlJAXB.writeValueAsString(response)));

        mockServer.expect(once(), requestTo(RESPONSE_DELETE_URL))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_XML)
                        .body(mapperXml.writeValueAsString("Response " + response.getUuid() + "deleted")));

        MvcResult mvcResult = mockMvc.perform(post(FINE_GET_URL)
                        .accept(MediaType.TEXT_XML_VALUE)
                        .contentType(MediaType.APPLICATION_XML)
                        .content(MapperXmlJAXB.writeValueAsString(request)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType("text/xml;charset=UTF-8"))
                .andDo(print())
                .andReturn();
        Fine fine = MapperXmlJAXB.readValueAsString(
                mvcResult.getResponse().getContentAsString(),
                Fine.class);
        assertEquals(request.getNumber(), fine.getNumber());
        assertEquals(request.getType(), fine.getType());
    }
}
