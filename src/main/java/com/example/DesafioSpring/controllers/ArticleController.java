package com.example.DesafioSpring.controllers;

import com.example.DesafioSpring.dto.Articles;
import com.example.DesafioSpring.exceptions.InvalidFilterException;
import com.example.DesafioSpring.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/articles")
    public ResponseEntity searchProducts(@RequestParam Map<String, String> filter) throws IOException, InvalidFilterException {
        return new ResponseEntity(articleService.searchProducts(filter), HttpStatus.OK);
    }

    @PostMapping("/purchase-request")
    public ResponseEntity purchaseRequest(@RequestBody Articles articles) throws IOException {
        return new ResponseEntity(articleService.purchaseRequest(articles), HttpStatus.OK);}

    //parte del Bonus que llegue a terminar
    //para probarlo solo apuntar al endpoint /shopping-cart/purchase-request y enviarle los articulos de igual manera que el /purchase-request
    //devolvera las suma de los articulos enviados.
    @PostMapping("/shopping-cart/purchase-request")
    public ResponseEntity shoppingCart(@RequestBody Articles articles) throws IOException {
        return new ResponseEntity(articleService.shoppingCart(articles), HttpStatus.OK);}
}
