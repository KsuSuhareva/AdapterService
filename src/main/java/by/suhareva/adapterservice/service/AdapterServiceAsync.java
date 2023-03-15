package by.suhareva.adapterservice.service;

import by.suhareva.adapterservice.model.Fine;
import by.suhareva.adapterservice.model.GetResponse;
import by.suhareva.adapterservice.model.SendRequest;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AdapterServiceAsync {
    Fine getFine(SendRequest fine);

    SendRequest saveRequest(SendRequest sendRequest);

    GetResponse getResponseByIdRequest(SendRequest response);

    void deleteResponse(GetResponse response);
}
