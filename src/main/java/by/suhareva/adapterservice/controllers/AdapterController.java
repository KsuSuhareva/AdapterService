package by.suhareva.adapterservice.controllers;

import by.suhareva.adapterservice.model.Fine;
import by.suhareva.adapterservice.model.SendRequest;
import by.suhareva.adapterservice.service.AdapterServiceAsync;
import by.suhareva.adapterservice.service.AdapterServiceSync;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/adapter/")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdapterController {

    private final AdapterServiceSync serviceSync;
    private final AdapterServiceAsync serviceAsync;


    @PostMapping("getFineRestTemplate")
    public ResponseEntity<Fine> getFineRT(@Valid @RequestBody SendRequest request) {
        Fine fine = serviceAsync.getFine(request);
        return new ResponseEntity<>(fine, HttpStatus.OK);
    }


    @PostMapping("getFineWebClient")
    public ResponseEntity<Mono<Fine>> getFine(@Valid @RequestBody SendRequest request) {
        Mono<Fine> getResponseMono = serviceSync.getFine(request);
        return new ResponseEntity<>(getResponseMono, HttpStatus.OK);
    }

}
