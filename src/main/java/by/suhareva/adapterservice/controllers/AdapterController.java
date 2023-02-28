package by.suhareva.adapterservice.controllers;

import by.suhareva.adapterservice.model.Fine;
import by.suhareva.adapterservice.model.SendRequest;
import by.suhareva.adapterservice.service.AdapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/adapter/")
public class AdapterController {

    private final AdapterService service;

    @PostMapping("request")
    public Mono<Fine> getFine(@Valid @RequestBody SendRequest request) {
        Mono getResponseMono = service.getFine(request);
        return getResponseMono;
    }
}
