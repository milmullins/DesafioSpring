package com.example.DesafioSpring.services;

import com.example.DesafioSpring.dto.ArticleDTO;
import com.example.DesafioSpring.dto.Articles;
import com.example.DesafioSpring.dto.ResponseDTO;
import com.example.DesafioSpring.exceptions.InvalidFilterException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ArticleService {
    List<ArticleDTO> searchProducts(Map<String, String> filter) throws IOException, InvalidFilterException;
    ResponseDTO purchaseRequest(Articles articles) throws IOException;
    ResponseDTO shoppingCart(Articles articles) throws IOException;
}
