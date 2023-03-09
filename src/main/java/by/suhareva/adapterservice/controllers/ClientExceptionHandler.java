package by.suhareva.adapterservice.controllers;

import by.suhareva.adapterservice.exceptions.ErrorMassage;
import by.suhareva.adapterservice.exceptions.FineNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Date;

import static org.springframework.http.HttpStatus.*;


@RestControllerAdvice
@Slf4j
public class ClientExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMassage> catchMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Catch exception  MethodArgumentNotValidException {}", e.getMessage());
        HttpStatus status = BAD_REQUEST;
        ErrorMassage errorMassage = ErrorMassage.builder()
                .date(new Date())
                .status(status.value())
                .massage("The request must have  format: for legal entities '1234567890', for individuals- '12AB123456'")
                .cause(e.getClass().getSimpleName()).build();
        return new ResponseEntity<>(errorMassage, status);
    }

    @ExceptionHandler(WebClientResponseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMassage> catchFineNotFoundException(WebClientException e) {
        log.error("Catch exception  WebClientResponseException with cause:{} ", e.getMessage());
        HttpStatus status = INTERNAL_SERVER_ERROR;
        ErrorMassage errorMassage = ErrorMassage.builder()
                .date(new Date())
                .status(status.value())
                .massage(e.getMessage())
                .cause(e.getClass().getSimpleName()).build();
        return new ResponseEntity<>(errorMassage, status);
    }

    @ExceptionHandler(FineNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMassage> catchFineNotFoundException(FineNotFoundException e) {
        log.error("Catch exception  FineNotFoundException with cause:{}", e.getMessage());
        HttpStatus status = NOT_FOUND;
        ErrorMassage errorMassage = ErrorMassage.builder()
                .date(new Date())
                .status(status.value())
                .massage(e.getMessage())
                .cause(e.getClass().getSimpleName()).build();
        return new ResponseEntity<>(errorMassage, status);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMassage> catchAnyException(Exception e) {
        log.error("Catch exception  Exception with cause:{} ", e.getMessage());
        HttpStatus status = INTERNAL_SERVER_ERROR;
        ErrorMassage massage = new ErrorMassage(new Date(), status.value(), e.getMessage(), e.getClass().getSimpleName());
        return new ResponseEntity<>(massage, status);
    }


}
