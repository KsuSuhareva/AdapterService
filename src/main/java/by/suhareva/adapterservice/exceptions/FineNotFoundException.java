package by.suhareva.adapterservice.exceptions;

import lombok.Getter;

@Getter
public class FineNotFoundException extends RuntimeException{

    public FineNotFoundException(String message) {
        super(message);
    }
}
