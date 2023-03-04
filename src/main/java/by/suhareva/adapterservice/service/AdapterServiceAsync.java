package by.suhareva.adapterservice.service;

import by.suhareva.adapterservice.model.Fine;
import by.suhareva.adapterservice.model.GetResponse;
import by.suhareva.adapterservice.model.SendRequest;

import java.util.UUID;

public interface AdapterServiceAsync {
    Fine getFine(SendRequest fine);
    UUID saveRequest(SendRequest sendRequest);
    GetResponse getResponseByIdRequest(UUID uuid);

    void deleteResponse(UUID id);
}
