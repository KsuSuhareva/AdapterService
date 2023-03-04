package by.suhareva.adapterservice.service.AsyncImpl;

import by.suhareva.adapterservice.model.Fine;
import by.suhareva.adapterservice.model.GetResponse;
import by.suhareva.adapterservice.model.SendRequest;
import by.suhareva.adapterservice.service.AdapterServiceAsync;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdapterServiceRestTemplate implements AdapterServiceAsync {
    @Autowired
    private final RestTemplate restTemplate;
    @Autowired
    private final RetryTemplate retry;

    @Override
    public Fine getFine(SendRequest sendRequest) {
        UUID uuidRequest = saveRequest(sendRequest);
        GetResponse response = getResponseByIdRequest(uuidRequest);
        Fine fine = response.getFine(sendRequest.getNumber());
        deleteResponse(response.getUuid());
        return fine;
    }

    @Override
    public UUID saveRequest(SendRequest sendRequest) {
        log.info("Request {} send to save to SVEM ", sendRequest);
        return restTemplate.postForObject("/request/save/", sendRequest, UUID.class);
    }

    @Override
    public GetResponse getResponseByIdRequest(UUID uuid) {
        log.info("Request id={} send to SVEM for get response", uuid);
        return retry.execute(r -> restTemplate.postForObject("/request/getResponse/", uuid, GetResponse.class));
    }

    @Override
    public void deleteResponse(UUID id) {
        log.info("Response id={}  send to SVEM for delete", id);
        restTemplate.postForObject("/request/delete/", id, String.class);
    }

}
