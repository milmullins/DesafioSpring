package com.example.DesafioSpring.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ArticleNotFoundException extends ResponseStatusException {
    public ArticleNotFoundException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
