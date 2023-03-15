package by.suhareva.adapterservice.service;

import by.suhareva.adapterservice.model.Fine;
import by.suhareva.adapterservice.model.GetResponse;
import by.suhareva.adapterservice.model.SendRequest;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AdapterServiceSync {
    Mono<Fine> getFine(SendRequest fine);

    Mono<SendRequest> saveRequest(SendRequest sendRequest);

    Mono<GetResponse> getResponseByIdRequest(SendRequest response);

    void deleteResponse(GetResponse response);


}
