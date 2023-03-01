package by.suhareva.adapterservice.service;

import by.suhareva.adapterservice.model.Fine;
import by.suhareva.adapterservice.model.GetResponse;
import by.suhareva.adapterservice.model.SendRequest;
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
public class AdapterServiceImpl implements AdapterService {

    private final WebClient.Builder webClientBuilder;

    public Mono<Fine> getFine(SendRequest sendRequest) {
        Mono<UUID> uuidRequest = getUuidRequest(sendRequest);
        Mono<GetResponse> response = uuidRequest.flatMap(this::getResponseByIdRequest);
        Mono<Fine> fineMono = response.map(r -> r.getFine(sendRequest.getNumber()));
        response.subscribe(r -> deleteResponse(r.getUuid()));
        return fineMono;
    }

    public Mono<UUID> getUuidRequest(SendRequest sendRequest) {
        log.info("Request {} send to save to SVEM ", sendRequest);
        Mono<UUID> uuid = webClientBuilder.build()
                .post()
                .uri("/request/save/")
                .bodyValue(sendRequest)
                .retrieve()
                .bodyToMono(UUID.class)
                .cache();
        return uuid;
    }

    @Override
    public Mono<GetResponse> getResponseByIdRequest(UUID uuid) {
        log.info("Request id={} send to SVEM for get response", uuid);
        Mono<GetResponse> getResponseMono = webClientBuilder.build()
                .post()
                .uri("/request/getResponse/" )
                .bodyValue(uuid)
                .retrieve()
                .bodyToMono(GetResponse.class)
                .retryWhen(Retry.backoff(3, Duration.ofMillis(400)).jitter(0.75));
        return getResponseMono;
    }

    @Override
    public void deleteResponse(UUID id) {
        log.info("Response id={}  send to SVEM for delete", id);
        webClientBuilder.build()
                .post()
                .uri("/request/delete/" )
                .bodyValue(id)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(log::info);
    }
}
