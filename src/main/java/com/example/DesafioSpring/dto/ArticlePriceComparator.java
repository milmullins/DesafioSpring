package com.example.DesafioSpring.dto;

import java.util.Comparator;

public class ArticlePriceComparator implements Comparator<ArticleDTO> {
    @Override
    public int compare(ArticleDTO a1, ArticleDTO a2) {
        if(a1.getPrice()==a2.getPrice())
            return 0;
        else if(a1.getPrice()<a2.getPrice())
            return 1;
        else
            return -1;
    }
}
