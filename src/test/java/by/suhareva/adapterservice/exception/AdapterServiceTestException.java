package by.suhareva.adapterservice.exception;

import by.suhareva.adapterservice.exceptions.FineNotFoundException;
import by.suhareva.adapterservice.model.SendRequest;
import by.suhareva.adapterservice.service.AsyncImpl.AdapterServiceRestTemplate;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.UUID;

import static by.suhareva.adapterservice.enums.ClientType.INDIVIDUAL;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class AdapterServiceTestException {

    @MockBean
    private AdapterServiceRestTemplate service;

    @Test
    public void getResponseByIdRequestThrowResponseNotFoundException() throws Exception {
        SendRequest request = new SendRequest(null, "12AA123", INDIVIDUAL);
        Mockito.when(service.getFine(any(SendRequest.class))).thenThrow(FineNotFoundException.class);
        assertThrows(FineNotFoundException.class, () -> service.getFine(request));
    }
}
