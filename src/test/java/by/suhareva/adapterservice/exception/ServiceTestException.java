package by.suhareva.adapterservice.exception;

import by.suhareva.adapterservice.exceptions.FineNotFoundException;
import by.suhareva.adapterservice.model.SendRequest;
import by.suhareva.adapterservice.service.AsyncImpl.AdapterServiceRestTemplateImpl;
import by.suhareva.adapterservice.service.SyncImpl.AdapterServiceWebClientImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static by.suhareva.adapterservice.enums.ClientType.INDIVIDUAL;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class ServiceTestException {

    @MockBean
    private AdapterServiceRestTemplateImpl serviceRestTemplate;
    @MockBean
    private AdapterServiceWebClientImpl serviceWebClient ;

    @Test
    public void getResponseByIdRequestThrowResponseNotFoundExceptionRestTemplate() throws Exception {
        SendRequest request = new SendRequest(null, "12AA123", INDIVIDUAL);
        Mockito.when(serviceRestTemplate.getFine(any(SendRequest.class))).thenThrow(FineNotFoundException.class);
        assertThrows(FineNotFoundException.class, () -> serviceRestTemplate.getFine(request));
    }

    @Test
    public void getResponseByIdRequestThrowResponseNotFoundExceptionWebClient() throws Exception {
        SendRequest request = new SendRequest(null, "12AA123", INDIVIDUAL);
        Mockito.when(serviceWebClient.getFine(any(SendRequest.class))).thenThrow(FineNotFoundException.class);
        assertThrows(FineNotFoundException.class, () -> serviceWebClient.getFine(request));
    }
}
