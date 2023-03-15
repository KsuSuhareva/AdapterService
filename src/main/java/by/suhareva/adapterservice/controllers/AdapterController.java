package by.suhareva.adapterservice.controllers;

import by.suhareva.adapterservice.model.Fine;
import by.suhareva.adapterservice.model.SendRequest;
import by.suhareva.adapterservice.service.AdapterServiceAsync;
import by.suhareva.adapterservice.service.AdapterServiceSync;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Tag(name = "AdapterController", description = "The controller accepts a client request and allows to get information about fines from another application")
@RestController
@RequiredArgsConstructor
@RequestMapping("/adapter/")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdapterController {

    private final AdapterServiceSync serviceSync;
    private final AdapterServiceAsync serviceAsync;

    @Operation(summary = "The method gets a fine",
            description = "Method allows to get  fines using asynchronous mode from another application")
    @PostMapping("getFineRestTemplate")
    public ResponseEntity<Fine> getFineRestTemplate(@Valid @RequestBody SendRequest request) {
        Fine fine = serviceAsync.getFine(request);
        return new ResponseEntity<>(fine, HttpStatus.OK);
    }

    @Operation(summary = "The method gets a fine",
            description = "Method allows to get  fines using synchronous mode from another application")
    @PostMapping("getFineWebClient")
    public ResponseEntity<Mono<Fine>> getFineWebClient(@Valid @RequestBody SendRequest request) {
        Mono<Fine> getResponseMono = serviceSync.getFine(request);
        return new ResponseEntity<>(getResponseMono, HttpStatus.OK);
    }

}
