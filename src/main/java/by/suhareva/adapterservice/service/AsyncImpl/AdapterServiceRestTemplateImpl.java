package by.suhareva.adapterservice.service.AsyncImpl;

import by.suhareva.adapterservice.model.Fine;
import by.suhareva.adapterservice.model.GetResponse;
import by.suhareva.adapterservice.model.SendRequest;
import by.suhareva.adapterservice.service.AdapterServiceAsync;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdapterServiceRestTemplateImpl implements AdapterServiceAsync {

    private final RestTemplate restTemplate;
    private final RetryTemplate retry;

    @Override
    public Fine getFine(SendRequest sendRequest) {
        SendRequest request = saveRequest(sendRequest);
        GetResponse response = getResponseByIdRequest(request);
        Fine fine = response.getFine(sendRequest.getNumber());
        deleteResponse(response);
        return fine;
    }

    @Override
    public SendRequest saveRequest(SendRequest sendRequest) {
        log.info("Request {} send to save to SVEM ", sendRequest);
        return restTemplate.postForObject("/request/save", sendRequest, SendRequest.class);
    }

    @Override
    public GetResponse getResponseByIdRequest(SendRequest sendRequest) {
        log.info("Request id={} send to SVEM for get response", sendRequest.getUuid());
        return retry.execute(r -> restTemplate.postForObject("/request/getResponse", sendRequest, GetResponse.class));
    }

    @Override
    public void deleteResponse(GetResponse response) {
        log.info("Response id={}  send to SVEM for delete", response.getUuid());
        restTemplate.postForObject("/request/delete", response, String.class);
    }

}
