package com.example.DesafioSpring.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OutOfStockException extends ResponseStatusException {
    public OutOfStockException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
