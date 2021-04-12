package com.example.DesafioSpring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO extends ArticleResponseDTO implements Comparable<ArticleDTO>{

    private String category;
    private double price;
    private boolean freeShipping;
    private int prestige;

    @Override
    public int compareTo(ArticleDTO article) {
        return this.getName().compareTo(article.getName());
    }
}
