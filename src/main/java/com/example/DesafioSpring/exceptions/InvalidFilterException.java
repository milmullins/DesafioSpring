package com.example.DesafioSpring.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidFilterException extends ResponseStatusException {
    public InvalidFilterException(HttpStatus code,String message) {
        super(code,message);
    }
}
