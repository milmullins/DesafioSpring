package com.example.DesafioSpring.repositories;

import com.example.DesafioSpring.dto.ArticleDTO;

import java.io.IOException;
import java.util.List;

public interface ArticleRepository {
    List<ArticleDTO> searchProducts() throws IOException;
    void updateCSV(String idProducToActualice, String quantityOld, String quantityNew) throws IOException;
}
