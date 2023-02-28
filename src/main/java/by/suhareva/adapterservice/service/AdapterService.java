package by.suhareva.adapterservice.service;

import by.suhareva.adapterservice.model.Fine;
import by.suhareva.adapterservice.model.GetResponse;
import by.suhareva.adapterservice.model.SendRequest;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AdapterService {
    Mono<Fine> getFine(SendRequest fine);

    Mono<GetResponse> getResponseByIdRequest(UUID uuid);

    void deleteResponse(UUID id);


}
