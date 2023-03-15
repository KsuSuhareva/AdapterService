package by.suhareva.adapterservice.service.SyncImpl;

import by.suhareva.adapterservice.model.Fine;
import by.suhareva.adapterservice.model.GetResponse;
import by.suhareva.adapterservice.model.SendRequest;
import by.suhareva.adapterservice.service.AdapterServiceSync;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdapterServiceWebClientImpl implements AdapterServiceSync {

    private final WebClient.Builder webClientBuilder;

    public Mono<Fine> getFine(SendRequest sendRequest) {
        Mono<SendRequest> request = saveRequest(sendRequest);
        Mono<GetResponse> response = request.flatMap(this::getResponseByIdRequest);
        Mono<Fine> fineMono = response.map(r -> r.getFine(sendRequest.getNumber()));
        response.subscribe(this::deleteResponse);
        return fineMono;
    }
    @Override
    public Mono<SendRequest> saveRequest(SendRequest sendRequest) {
        log.info("Request {} send to save to SVEM ", sendRequest);
        return webClientBuilder.build()
                .post()
                .uri("/request/save")
                .bodyValue(sendRequest)
                .retrieve()
                .bodyToMono(SendRequest.class)
                .cache();
    }

    @Override
    public Mono<GetResponse> getResponseByIdRequest(SendRequest request) {
        log.info("Request id={} send to SVEM for get response", request.getUuid());
        return webClientBuilder.build()
                .post()
                .uri("/request/getResponse")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GetResponse.class)
                .retryWhen(Retry.backoff(3, Duration.ofMillis(200)).jitter(0.75));
    }

    @Override
    public void deleteResponse(GetResponse response) {
        log.info("Response id={}  send to SVEM for delete", response.getUuid());
        webClientBuilder.build()
                .post()
                .uri("/request/delete")
                .bodyValue(response)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(log::info);
    }
}
